# fans API smoke test v2 (auth enabled, unique data per run)
# Usage: powershell -NoProfile -ExecutionPolicy Bypass -File tools\smoke.ps1
$ErrorActionPreference = 'Continue'
$base = 'http://127.0.0.1:8000'
$script:fail = 0
$script:pass = 0
$sfx = [guid]::NewGuid().ToString('N').Substring(0, 6)

function Call([string]$method, [string]$path, $obj, $token) {
    $headers = @{}
    if ($token) { $headers['Authorization'] = $token }
    try {
        if ($method -eq 'GET') {
            $r = Invoke-WebRequest -Uri "$base$path" -Headers $headers -UseBasicParsing
        } else {
            $json = if ($null -eq $obj) { '{}' } else { $obj | ConvertTo-Json -Depth 12 -Compress }
            $bytes = [Text.Encoding]::UTF8.GetBytes($json)
            $r = Invoke-WebRequest -Uri "$base$path" -Method Post -ContentType 'application/json; charset=utf-8' -Body $bytes -Headers $headers -UseBasicParsing
        }
        return [pscustomobject]@{ status = [int]$r.StatusCode; body = $r.Content }
    } catch {
        $code = 0
        if ($_.Exception.Response) { $code = [int]$_.Exception.Response.StatusCode }
        $body = ''
        if ($_.ErrorDetails) { $body = $_.ErrorDetails.Message }
        return [pscustomobject]@{ status = $code; body = $body }
    }
}

function Check([string]$name, $resp, [int]$expectHttp = 200, [int]$expectBiz = 200) {
    if ($resp.status -ne $expectHttp) {
        Write-Host ("[FAIL] {0} -> http={1} body={2}" -f $name, $resp.status, $resp.body) -ForegroundColor Red
        $script:fail++
        return $null
    }
    if ($expectBiz -lt 0) {
        Write-Host ("[ OK ] {0} -> http={1}" -f $name, $resp.status)
        $script:pass++
        return $null
    }
    if ([string]::IsNullOrWhiteSpace($resp.body)) {
        Write-Host ("[ OK ] {0} (empty body)" -f $name)
        $script:pass++
        return $null
    }
    try {
        $json = $resp.body | ConvertFrom-Json
    } catch {
        Write-Host ("[FAIL] {0} -> non-json: {1}" -f $name, $resp.body) -ForegroundColor Red
        $script:fail++
        return $null
    }
    if ($null -ne $json.code -and $json.code -ne $expectBiz) {
        Write-Host ("[FAIL] {0} -> code={1} body={2}" -f $name, $json.code, $resp.body) -ForegroundColor Red
        $script:fail++
        return $json
    }
    Write-Host ("[ OK ] {0}" -f $name)
    $script:pass++
    return $json
}

Write-Host '=== auth ==='
$deny = Call 'GET' '/landing/gtl' $null $null
Check 'protected without token -> 401' $deny 401 -1
$deny2 = Call 'GET' '/admin/profile?id=1' $null $null
Check 'profile without token -> 401' $deny2 401 -1

$loginResp = Call 'POST' '/admin/login' @{ form = @{ username = 'admin'; password = '123456' } } $null
$login = Check 'POST /admin/login (admin/123456)' $loginResp
$tk = $null
if ($login) { $tk = $login.Token; Write-Host ("       token length = {0}, IDcode = {1}" -f $tk.Length, $login.IDcode) }

$profile = Check 'GET /admin/profile (with token)' (Call 'GET' "/admin/profile?id=$($login.IDcode)" $null $tk)
if ($profile) {
    $hasPwd = $profile.data.PSObject.Properties.Name -contains 'password'
    if ($hasPwd) { Write-Host '[FAIL] /admin/profile still returns password' -ForegroundColor Red; $script:fail++ }
    else { Write-Host ("       username = {0}, no password field" -f $profile.data.username); $script:pass++ }
}
Check 'GET /admin/user removed -> 404' (Call 'GET' '/admin/user?id=1' $null $tk) 404 -1

Write-Host '=== group ==='
Check 'POST /group/append' (Call 'POST' '/group/append' @{ forms = @{ group = "smoke-group-$sfx"; remarks = 'smoke' } } $tk)
$groups = Check 'GET /group/gtgroup' (Call 'GET' '/group/gtgroup?currentpage=1&singlepage=10' $null $tk)
$groupId = ($groups.data.data | Where-Object { $_.group -eq "smoke-group-$sfx" }).id
Check 'GET /group/gta' (Call 'GET' '/group/gta' $null $tk)
Check 'GET /group/gts' (Call 'GET' "/group/gts?words=$sfx&currentpage=1&singlepage=10" $null $tk)
Check 'POST /group/update' (Call 'POST' '/group/update' @{ forms = @{ id = $groupId; group = "smoke-group2-$sfx"; remarks = 'updated' } } $tk)

Write-Host '=== landing ==='
$url = "https://smoke.test/$sfx"
$wxh = "wx_$sfx"
Check 'POST /landing/append' (Call 'POST' '/landing/append' @{ data = @{ landingurl = $url; group = $groupId; remarks = 'r'; delivery = 1; track = 0; open = 1 } } $tk)
$landings = Check 'POST /landing/gtlanding' (Call 'POST' '/landing/gtlanding' @{ currentpage = 1; singlepage = 10; condition = @{} } $tk)
$landingId = ($landings.data | Where-Object { $_.url -eq $url }).id
Check 'GET /landing/gtl' (Call 'GET' '/landing/gtl' $null $tk)
Check 'POST /landing/tgl' (Call 'POST' '/landing/tgl' @{ id = $landingId; enable = 0 } $tk)
Check 'POST /landing/lwg' (Call 'POST' '/landing/lwg' @{ currentpage = 1; singlepage = 10; condition = @{} } $tk)
Check 'POST /landing/lar' (Call 'POST' '/landing/lar' @{ currentpage = 1; singlepage = 10; condition = @{ order = 'total' } } $tk)
Check 'POST /landing/gtr' (Call 'POST' '/landing/gtr' @{ currentpage = 1; singlepage = 10; condition = @{} } $tk)
Check 'POST /landing/update' (Call 'POST' '/landing/update' @{ data = @{ id = $landingId; url = $url; group = $groupId; remarks = 'r2'; delivery = 1; track = 1; enable = 1 } } $tk)
Check 'POST /landing/cgwx' (Call 'POST' '/landing/cgwx' @{ data = @{ id = $landingId; erweima = 'qr.png'; from = 'src'; group = $groupId; same = 's'; weixin = $wxh; mclick = 'a'; mtouch = 'b'; pclick = 'c'; pselect = 'd'; show1 = $true; show2 = $false; share = 1 } } $tk)
$lag = Call 'GET' '/landing/lag' $null $tk
if ($lag.status -eq 200) {
    $lagJson = $lag.body | ConvertFrom-Json
    Write-Host ("[ OK ] GET /landing/lag -> {0} rows (bare array)" -f $lagJson.Count)
    $script:pass++
} else {
    Write-Host ("[FAIL] GET /landing/lag -> {0}" -f $lag.status) -ForegroundColor Red
    $script:fail++
}

Write-Host '=== cvstype ==='
$typeInt = Get-Random -Minimum 1000 -Maximum 9000
Check 'POST /cvstype/append' (Call 'POST' '/cvstype/append' @{ data = @{ type = "smoke-type-$sfx"; int = $typeInt } } $tk)
$types = Check 'GET /cvstype/gtl' (Call 'GET' '/cvstype/gtl' $null $tk)
$typeId = ($types.data | Where-Object { $_.typeint -eq $typeInt }).id
Check 'GET /cvstype/tpl' (Call 'GET' '/cvstype/tpl' $null $tk)
Check 'GET /cvstype/upn' (Call 'GET' "/cvstype/upn?id=$typeId&typename=smoke-type2" $null $tk)
Check 'GET /cvstype/del' (Call 'GET' "/cvstype/del?id=$typeId" $null $tk)

Write-Host '=== cvslink ==='
Check 'POST /cvslink/append' (Call 'POST' '/cvslink/append' @{ data = @{ uid = $landingId; type = '1'; cvstype = 1; cvscount = 0; cvsmode = 0; code = 'type_index="1"' } } $tk)
$links = Check 'POST /cvslink/gbl' (Call 'POST' '/cvslink/gbl' @{ currentpage = 1; singlepage = 10; condition = @{} } $tk)
$linkId = ($links.data | Where-Object { $_.uid -eq $landingId }).id
Check 'POST /cvslink/gtr' (Call 'POST' '/cvslink/gtr' @{ currentpage = 1; singlepage = 10; condition = @{} } $tk)
Check 'POST /cvslink/min' (Call 'POST' '/cvslink/min' @{ data = @{ id = $linkId; second = 15 } } $tk)
$flg = Call 'POST' '/cvslink/flg' @{ words = 'demo' } $null
if ($flg.status -eq 200) {
    Write-Host '[ OK ] POST /cvslink/flg (public, raw object)'
    $script:pass++
} else {
    Write-Host ("[FAIL] POST /cvslink/flg -> {0} {1}" -f $flg.status, $flg.body) -ForegroundColor Red
    $script:fail++
}

Write-Host '=== wxgroup / weixin ==='
Check 'POST /wxgroup/append' (Call 'POST' '/wxgroup/append' @{ data = @{ group = "smoke-wxgroup-$sfx"; remark = 'r' } } $tk)
$wxgroups = Check 'GET /wxgroup/gtg' (Call 'GET' '/wxgroup/gtg?currentpage=1&singlepage=10' $null $tk)
$wxgroupId = ($wxgroups.data | Where-Object { $_.name -eq "smoke-wxgroup-$sfx" }).id
Check 'GET /wxgroup/ggl' (Call 'GET' '/wxgroup/ggl' $null $tk)
Check 'POST /weixin/append' (Call 'POST' '/weixin/append' @{ data = @{ wxh = $wxh; wxname = 'demo'; erweima = 'qr.png'; sex = '1'; wxgroup = $wxgroupId; online = 1; auto = 1; level = 2; remarks = 'r' } } $tk)
$wx = Check 'POST /weixin/ctwx' (Call 'POST' '/weixin/ctwx' @{ currentpage = 1; singlepage = 10; condition = @{ words = $sfx } } $tk)
$wxId = ($wx.data | Where-Object { $_.wxh -eq $wxh }).id
if ($wx) { Write-Host ("       online = {0}, control = {1}, copy = {2}, Wxgroup.name = {3}" -f $wx.data[0].online, $wx.data[0].control, $wx.data[0].copy, $wx.data[0].Wxgroup.name) }
Check 'POST /weixin/getwx' (Call 'POST' '/weixin/getwx' @{ currentpage = 1; singlepage = 10; condition = @{} } $tk)
Check 'POST /weixin/udwx' (Call 'POST' '/weixin/udwx' @{ data = @{ id = $wxId; wxh = $wxh; wxname = 'demo2'; erweima = 'qr2.png'; sex = '1'; wxgroup = $wxgroupId; online = 0; auto = 2; level = 3; remarks = 'r2' } } $tk)
Check 'POST /weixin/udqr' (Call 'POST' '/weixin/udqr' @{ data = @{ id = $wxId; sendimg = 'qr3.png' } } $tk)
Check 'GET /weixin/delqr' (Call 'GET' "/weixin/delqr?id=$wxId" $null $tk)
Check 'GET /weixin/cjol' (Call 'GET' "/weixin/cjol?id=$wxId&online=1" $null $tk)
Check 'GET /weixin/cjlv' (Call 'GET' "/weixin/cjlv?id=$wxId&level=9" $null $tk)

Write-Host '=== monitor ==='
Check 'POST /monitor/append (public)' (Call 'POST' '/monitor/append' @{ url = $landingId; cvsdate = (Get-Date -Format 'yyyy-MM-dd'); cvstime = (Get-Date -Format 'HH:mm:ss'); stay = 12; type = 1; targetelement = 'btn'; city = @{ cip = '1.2.3.4' }; equipment = 'ios'; rate = '1'; wx = $wxh; search = 's'; total = 1; report = 0 } $null) 200 -1
Check 'POST /monitor/inctotal (public)' (Call 'POST' '/monitor/inctotal' @{ url = $landingId; wx = $wxh } $null) 200 -1
Check 'POST /monitor/gtmonitor' (Call 'POST' '/monitor/gtmonitor' @{ currentpage = 1; singlepage = 10; condition = @{} } $tk)
Check 'GET /monitor/mthome' (Call 'GET' '/monitor/mthome' $null $tk)

Write-Host '=== landing batch import ==='
$batchText = "https://batch.test/a`nhttps://batch.test/b`nnot a url`nhttps://batch.test/a"
$preview = Check 'POST /landing/import (preview)' (Call 'POST' '/landing/import' @{ data = @{ landingurls = $batchText; dryRun = $true; group = $groupId; open = $true } } $tk)
if ($preview) {
    Write-Host ("       total={0} valid={1} duplicate={2} invalid={3}" -f $preview.data.total, $preview.data.validCount, $preview.data.duplicates.Count, $preview.data.invalid.Count)
    if ($preview.data.validCount -ne 2 -or $preview.data.duplicates.Count -ne 1 -or $preview.data.invalid.Count -ne 1) {
        Write-Host '[FAIL] batch preview counts are wrong' -ForegroundColor Red
        $script:fail++
    }
}
$commit = Check 'POST /landing/import (commit)' (Call 'POST' '/landing/import' @{ data = @{ landingurls = $batchText; dryRun = $false; group = $groupId; open = $true } } $tk)
if ($commit) { Write-Host ("       inserted={0}" -f $commit.data.inserted) }
$batchList = Check 'POST /landing/gtlanding (batch rows)' (Call 'POST' '/landing/gtlanding' @{ currentpage = 1; singlepage = 10; condition = @{ words = 'batch.test' } } $tk)
if ($batchList) {
    foreach ($row in $batchList.data) {
        Call 'GET' "/landing/del?id=$($row.id)" $null $tk | Out-Null
    }
    Write-Host ("       imported rows = {0} (deleted)" -f $batchList.data.Count)
}

Write-Host '=== optimizations ==='
$sw = Call 'GET' '/swagger-ui/index.html' $null $null
if ($sw.status -eq 200) { Write-Host '[ OK ] GET /swagger-ui/index.html (public)'; $script:pass++ }
else { Write-Host ("[FAIL] swagger -> {0}" -f $sw.status) -ForegroundColor Red; $script:fail++ }

$ex = Call 'GET' '/export/landing.csv' $null $tk
if ($ex.status -eq 200 -and $ex.body.Length -gt 20) { Write-Host '[ OK ] GET /export/landing.csv'; $script:pass++ }
else { Write-Host ("[FAIL] export -> {0}" -f $ex.status) -ForegroundColor Red; $script:fail++ }
Check 'export without token -> 401' (Call 'GET' '/export/landing.csv' $null $null) 401 -1

# idempotency: same eventId must count once
$rankBefore = (Call 'POST' '/landing/lar' @{ currentpage = 1; singlepage = 50; condition = @{ order = 'total' } } $tk).body | ConvertFrom-Json
$demo = $rankBefore.data | Where-Object { $_.url -eq 'http://127.0.0.1:8000/demo/landing.html' }
if ($demo) {
    $evt = "evt-$sfx"
    $appendBody = @{ eventId = $evt; url = $demo.id; wx = 'wx_demo_live'; cvsdate = (Get-Date -Format 'yyyy-MM-dd'); cvstime = (Get-Date -Format 'HH:mm:ss'); type = 1; total = 1 }
    Call 'POST' '/monitor/append' $appendBody $null | Out-Null
    Call 'POST' '/monitor/append' $appendBody $null | Out-Null
    $rankAfter = (Call 'POST' '/landing/lar' @{ currentpage = 1; singlepage = 50; condition = @{ order = 'total' } } $tk).body | ConvertFrom-Json
    $demoAfter = $rankAfter.data | Where-Object { $_.id -eq $demo.id }
    if (($demoAfter.total - $demo.total) -eq 1) { Write-Host '[ OK ] monitor eventId idempotency (+1)'; $script:pass++ }
    else { Write-Host ("[FAIL] idempotency delta={0}" -f ($demoAfter.total - $demo.total)) -ForegroundColor Red; $script:fail++ }
}

# RBAC: operator sees only own data and cannot delete others'
$opName = "op_$sfx"
Call 'POST' '/admin/rgs' @{ form = @{ username = $opName; password = '123456'; power = 1 } } $null | Out-Null
$opLogin = Call 'POST' '/admin/login' @{ form = @{ username = $opName; password = '123456' } } $null
$opToken = $null
if ($opLogin.status -eq 200) { $opToken = ($opLogin.body | ConvertFrom-Json).Token }
if ($opToken) {
    $opList = (Call 'POST' '/landing/gtlanding' @{ currentpage = 1; singlepage = 10; condition = @{} } $opToken).body | ConvertFrom-Json
    if ($opList.count -eq 0) { Write-Host '[ OK ] operator sees only own data (0 rows)'; $script:pass++ }
    else { Write-Host ("[FAIL] operator sees {0} rows" -f $opList.count) -ForegroundColor Red; $script:fail++ }
    Check 'operator cannot delete others (403)' (Call 'GET' "/landing/del?id=$landingId" $null $opToken) 403 -1
}

Write-Host '=== access ==='
$idx = Call 'GET' '/access/index' $null $null
if ($idx.status -eq 200) { Write-Host ("[ OK ] GET /access/index -> {0}" -f $idx.body); $script:pass++ }
else { Write-Host ("[FAIL] GET /access/index -> {0}" -f $idx.status) -ForegroundColor Red; $script:fail++ }
Check 'GET /access/get (public)' (Call 'GET' '/access/get' $null $null) 200 -1

Write-Host '=== unbind / delete ==='
Check 'GET /landing/ubd' (Call 'GET' "/landing/ubd?id=$landingId" $null $tk)
Check 'POST /landing/ubl' (Call 'POST' '/landing/ubl' @{ arr = @($landingId) } $tk)
Check 'POST /landing/cgl' (Call 'POST' '/landing/cgl' @{ arr = @($landingId); form = @{ erweima = 'q'; from = 'f'; group = $groupId; same = 's'; weixin = 'w'; mclick = 'a'; mtouch = 'b'; pclick = 'c'; pselect = 'd'; share = 0; show1 = $true } } $tk)
Check 'GET /landing/del' (Call 'GET' "/landing/del?id=$landingId" $null $tk)
Check 'GET /cvslink/del' (Call 'GET' "/cvslink/del?id=$linkId" $null $tk)
Check 'GET /weixin/delwx' (Call 'GET' "/weixin/delwx?arr=$wxId" $null $tk)
Check 'GET /wxgroup/del' (Call 'GET' "/wxgroup/del?id=$wxgroupId" $null $tk)
Check 'GET /group/del' (Call 'GET' "/group/del?id=$groupId" $null $tk)

Write-Host ''
Write-Host ("=== passed: {0}, failed: {1} ===" -f $script:pass, $script:fail)
if ($script:fail -gt 0) { exit 1 }
