# fans schema migration v2 (idempotent, ASCII only)
# - new tables use Java-style camelCase columns
# - legacy columns are renamed (create_time -> createTime, `from` -> sourceUrl, ...)
# - unused legacy columns are dropped
# Usage: powershell -NoProfile -ExecutionPolicy Bypass -File db\migrate.ps1
$ErrorActionPreference = 'Stop'

. (Join-Path $PSScriptRoot '_env.ps1')
$sqlDir = ($PSScriptRoot -replace '\\', '/')

function Run-Sql([string]$sql) {
    & $mysql @base -e $sql
    if ($LASTEXITCODE -ne 0) { throw "SQL failed: $sql" }
}

function Column-Exists([string]$db, [string]$table, [string]$column) {
    $col = $column.Trim('`')
    $q = "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema='$db' AND table_name='$table' AND column_name='$col';"
    $out = & $mysql @base -N -e $q
    return ([int]($out | Select-Object -First 1)) -gt 0
}

function Add-Column([string]$db, [string]$table, [string]$column, [string]$definition) {
    if (Column-Exists $db $table $column) { return }
    Run-Sql ('ALTER TABLE {0}.{1} ADD COLUMN `{2}` {3};' -f $db, $table, $column.Trim('`'), $definition)
    Write-Host ("  + {0}.{1}.{2}" -f $db, $table, $column)
}

function Rename-Column([string]$db, [string]$table, [string]$old, [string]$new, [string]$definition) {
    $hasOld = Column-Exists $db $table $old
    $hasNew = Column-Exists $db $table $new
    if ($hasOld -and -not $hasNew) {
        Run-Sql ('ALTER TABLE {0}.{1} CHANGE COLUMN `{2}` `{3}` {4};' -f $db, $table, $old.Trim('`'), $new.Trim('`'), $definition)
        Write-Host ("  ~ {0}.{1}: {2} -> {3}" -f $db, $table, $old, $new)
    } elseif ($hasOld -and $hasNew) {
        Run-Sql ('UPDATE {0}.{1} SET `{2}` = `{3}` WHERE `{2}` IS NULL;' -f $db, $table, $new.Trim('`'), $old.Trim('`'))
        Run-Sql ('ALTER TABLE {0}.{1} DROP COLUMN `{2}`;' -f $db, $table, $old.Trim('`'))
        Write-Host ("  ~ {0}.{1}: merged {2} into {3}" -f $db, $table, $old, $new)
    }
}

function Drop-Column([string]$db, [string]$table, [string]$column) {
    if (-not (Column-Exists $db $table $column)) { return }
    Run-Sql ('ALTER TABLE {0}.{1} DROP COLUMN `{2}`;' -f $db, $table, $column.Trim('`'))
    Write-Host ("  - {0}.{1}.{2}" -f $db, $table, $column)
}

function Index-Exists([string]$db, [string]$table, [string]$index) {
    $q = "SELECT COUNT(*) FROM information_schema.statistics WHERE table_schema='$db' AND table_name='$table' AND index_name='$index';"
    $out = & $mysql @base -N -e $q
    return ([int]($out | Select-Object -First 1)) -gt 0
}

function Add-Index([string]$db, [string]$table, [string]$index, [string]$definition) {
    if (Index-Exists $db $table $index) { return }
    Run-Sql ('ALTER TABLE {0}.{1} ADD {2};' -f $db, $table, $definition)
    Write-Host ("  # {0}.{1}.{2}" -f $db, $table, $index)
}

Write-Host '== 1/5 create databases & tables (java style) =='
Run-Sql ("source {0}/01_tables.sql" -f $sqlDir)

Write-Host '== 2/5 add business columns =='
$adds = @(
    @('monitor_atr', 'edition_landing', 'remarks',  "VARCHAR(255) DEFAULT NULL"),
    @('monitor_atr', 'edition_landing', 'erweima',  "VARCHAR(500) DEFAULT NULL"),
    @('monitor_atr', 'edition_landing', 'weixin',   "VARCHAR(100) DEFAULT NULL"),
    @('monitor_atr', 'edition_landing', 'mclick',   "VARCHAR(255) DEFAULT NULL"),
    @('monitor_atr', 'edition_landing', 'mtouch',   "VARCHAR(255) DEFAULT NULL"),
    @('monitor_atr', 'edition_landing', 'pclick',   "VARCHAR(255) DEFAULT NULL"),
    @('monitor_atr', 'edition_landing', 'pselect',  "VARCHAR(255) DEFAULT NULL"),
    @('monitor_atr', 'edition_landing', 'share',    "TINYINT(1) DEFAULT '0'"),
    @('monitor_atr', 'edition_landing', 'same',     "VARCHAR(255) DEFAULT NULL"),
    @('monitor_atr', 'edition_landing', 'total',    "INT(11) DEFAULT '0'"),
    @('monitor_atr', 'edition_landing', 'cvscount', "INT(11) DEFAULT '0'"),
    @('monitor_atr', 'edition_landing', 'fans',     "INT(11) DEFAULT '0'"),
    @('monitor_atr', 'edition_landing', 'neardate', "DATETIME DEFAULT NULL"),

    @('monitor_atr', 'edition_group', 'remarks',    "VARCHAR(255) DEFAULT NULL"),
    @('monitor_atr', 'edition_group', 'total',      "INT(11) DEFAULT '0'"),

    @('wx_atr', 'manage_wxnumber', 'wxh',     "VARCHAR(100) DEFAULT NULL"),
    @('wx_atr', 'manage_wxnumber', 'wxname',  "VARCHAR(100) DEFAULT NULL"),
    @('wx_atr', 'manage_wxnumber', 'erweima', "VARCHAR(500) DEFAULT NULL"),
    @('wx_atr', 'manage_wxnumber', 'sex',     "VARCHAR(10) DEFAULT NULL"),
    @('wx_atr', 'manage_wxnumber', 'remarks', "VARCHAR(255) DEFAULT NULL"),
    @('wx_atr', 'manage_wxnumber', 'visits',  "INT(11) DEFAULT '0'"),
    @('wx_atr', 'manage_wxnumber', 'report',  "INT(11) DEFAULT '0'")
)
foreach ($a in $adds) { Add-Column $a[0] $a[1] $a[2] $a[3] }

# 二维码/头像可能以 base64 data-uri 形式提交，放宽为 MEDIUMTEXT
Run-Sql "ALTER TABLE wx_atr.manage_wxnumber MODIFY COLUMN erweima MEDIUMTEXT;"
Run-Sql "ALTER TABLE monitor_atr.edition_landing MODIFY COLUMN erweima MEDIUMTEXT;"
Run-Sql "ALTER TABLE monitor_atr.user_admin MODIFY COLUMN header MEDIUMTEXT;"

Write-Host '== 3/5 rename legacy columns to java style =='
$timestampTables = @(
    @('monitor_atr', 'edition_landing'),
    @('monitor_atr', 'edition_group'),
    @('monitor_atr', 'cvsmsg_link'),
    @('monitor_atr', 'cvsmsg_type'),
    @('monitor_atr', 'cvsmsg_one'),
    @('monitor_atr', 'user_admin'),
    @('wx_atr', 'manage_wxnumber'),
    @('wx_atr', 'manage_wxgroup'),
    @('wx_access', 'access_token')
)
foreach ($t in $timestampTables) {
    Rename-Column $t[0] $t[1] 'create_time' 'createTime' 'DATETIME DEFAULT NULL'
    Rename-Column $t[0] $t[1] 'update_time' 'updateTime' 'DATETIME DEFAULT NULL'
}

Rename-Column 'monitor_atr' 'edition_landing' '`from`' 'sourceUrl' 'VARCHAR(255) DEFAULT NULL'
Rename-Column 'monitor_atr' 'edition_landing' '`show`' 'visible'   "TINYINT(1) DEFAULT '0'"
Rename-Column 'monitor_atr' 'edition_group'   '`group`' 'groupName' 'VARCHAR(100) DEFAULT NULL'
Rename-Column 'wx_atr'       'manage_wxnumber' '`copy`' 'copyCount' "INT(11) DEFAULT '0'"

Write-Host '== 4/5 drop unused legacy columns =='
if (Column-Exists 'monitor_atr' 'edition_group' 'name') {
    Run-Sql "UPDATE monitor_atr.edition_group SET groupName = name WHERE (groupName IS NULL OR groupName = '') AND name IS NOT NULL;"
}
Drop-Column 'monitor_atr' 'edition_landing' 'name'
Drop-Column 'monitor_atr' 'edition_group'   'name'
Drop-Column 'wx_atr' 'manage_wxnumber' 'name'
Drop-Column 'wx_atr' 'manage_wxnumber' 'wxno'
Drop-Column 'wx_atr' 'manage_wxnumber' 'qrcode'

Write-Host '== 4.5/5 optimize: ownership / idempotency / types / indexes =='
foreach ($t in @(
    @('monitor_atr', 'edition_landing'),
    @('monitor_atr', 'edition_group'),
    @('monitor_atr', 'cvsmsg_link'),
    @('wx_atr', 'manage_wxnumber'),
    @('wx_atr', 'manage_wxgroup')
)) {
    Add-Column $t[0] $t[1] 'ownerId' 'INT(11) DEFAULT NULL'
}
Add-Column 'monitor_atr' 'cvsmsg_one' 'eventId' 'VARCHAR(64) DEFAULT NULL'

# 日期/时间列：varchar -> DATE / TIME（先清理空串，避免严格模式报错）
Run-Sql "UPDATE monitor_atr.cvsmsg_one SET cvsdate = NULL WHERE cvsdate = '';"
Run-Sql "UPDATE monitor_atr.cvsmsg_one SET cvstime = NULL WHERE cvstime = '';"
Run-Sql "ALTER TABLE monitor_atr.cvsmsg_one MODIFY COLUMN cvsdate DATE NULL;"
Run-Sql "ALTER TABLE monitor_atr.cvsmsg_one MODIFY COLUMN cvstime TIME NULL;"

Add-Index 'monitor_atr' 'edition_landing' 'idx_owner'  'KEY idx_owner (ownerId)'
Add-Index 'monitor_atr' 'edition_landing' 'idx_uid'    'KEY idx_uid (uid)'
Add-Index 'monitor_atr' 'edition_landing' 'idx_enable' 'KEY idx_enable (enable)'
Add-Index 'monitor_atr' 'edition_landing' 'idx_url'    'KEY idx_url (url(191))'
Add-Index 'monitor_atr' 'edition_group'   'idx_owner'  'KEY idx_owner (ownerId)'
Add-Index 'monitor_atr' 'edition_group'   'idx_name'   'KEY idx_name (groupName)'
Add-Index 'monitor_atr' 'cvsmsg_link'     'idx_owner'  'KEY idx_owner (ownerId)'
Add-Index 'monitor_atr' 'cvsmsg_link'     'idx_uid'    'KEY idx_uid (uid)'
Add-Index 'monitor_atr' 'cvsmsg_one'      'uk_event'   'UNIQUE KEY uk_event (eventId)'
Add-Index 'monitor_atr' 'cvsmsg_one'      'idx_wx'     'KEY idx_wx (wx)'
Add-Index 'wx_atr' 'manage_wxnumber' 'idx_owner'  'KEY idx_owner (ownerId)'
Add-Index 'wx_atr' 'manage_wxnumber' 'idx_uid'    'KEY idx_uid (uid)'
Add-Index 'wx_atr' 'manage_wxnumber' 'idx_online' 'KEY idx_online (online)'
Add-Index 'wx_atr' 'manage_wxgroup'  'idx_owner'  'KEY idx_owner (ownerId)'
Add-Index 'wx_atr' 'manage_wxgroup'  'idx_name'   'KEY idx_name (name)'
Add-Index 'monitor_atr' 'user_admin' 'idx_power'  'KEY idx_power (power)'

Write-Host '== 5/5 seed =='
Run-Sql ("source {0}/02_seed.sql" -f $sqlDir)
Run-Sql ("source {0}/03_admin.sql" -f $sqlDir)

Write-Host ''
Write-Host 'Done.'
