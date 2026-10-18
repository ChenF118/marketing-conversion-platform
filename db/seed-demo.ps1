# Seed demo data for the landing-page showcase (idempotent, ASCII only)
# Usage:
#   powershell -ExecutionPolicy Bypass -File db\seed-demo.ps1          # create if missing
#   powershell -ExecutionPolicy Bypass -File db\seed-demo.ps1 -Reset   # wipe stats and re-create
param([switch]$Reset)

$ErrorActionPreference = 'Stop'
. (Join-Path $PSScriptRoot '_env.ps1')
$dir = ($PSScriptRoot -replace '\\', '/')

if ($Reset) {
    Write-Host '[...] resetting demo data'
    & $mysql @base -e "source $dir/06_demo_reset.sql"
    if ($LASTEXITCODE -ne 0) { throw 'reset failed' }
}

& $mysql @base -e "source $dir/04_demo.sql"
if ($LASTEXITCODE -ne 0) { throw 'seed failed' }

Write-Host ''
Write-Host 'Demo rows:'
& $mysql @base -e "SELECT l.id AS landing_id, l.url, l.enable, w.wxh, w.wxname, w.online FROM monitor_atr.edition_landing l LEFT JOIN wx_atr.manage_wxnumber w ON w.wxh = l.weixin WHERE l.url = 'http://127.0.0.1:8000/demo/landing.html';"
& $mysql @base -e "SELECT c.id AS cvslink_id, c.uid AS landing_id, c.cvstype, c.cvsmode FROM monitor_atr.cvsmsg_link c JOIN monitor_atr.edition_landing l ON l.id = c.uid WHERE l.url = 'http://127.0.0.1:8000/demo/landing.html';"

Write-Host ''
Write-Host 'Open in browser:  http://127.0.0.1:8000/demo/landing.html'
