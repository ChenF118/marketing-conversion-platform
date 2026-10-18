# Shared MySQL connection settings.
# Reads Fans/.env (git-ignored) so no credentials are stored in the repository.
$repoRoot = Split-Path $PSScriptRoot -Parent
$envFile = Join-Path $repoRoot '.env'
if (Test-Path $envFile) {
    Get-Content $envFile -Encoding UTF8 | ForEach-Object {
        if ($_ -match '^\s*([A-Za-z_][A-Za-z0-9_]*)\s*=\s*(.*)\s*$') {
            $name = $matches[1]
            $value = $matches[2].Trim()
            if (-not [Environment]::GetEnvironmentVariable($name, 'Process')) {
                [Environment]::SetEnvironmentVariable($name, $value, 'Process')
            }
        }
    }
}

$dbHost = if ($env:DB_HOST) { $env:DB_HOST } else { '127.0.0.1' }
$dbPort = if ($env:DB_PORT) { $env:DB_PORT } else { '3306' }
$dbUser = if ($env:DB_USERNAME) { $env:DB_USERNAME } else { 'root' }
if (-not $env:DB_PASSWORD) {
    throw 'DB_PASSWORD is not set. Copy .env.example to .env and fill in your database password.'
}

$mysql = (Get-Command mysql.exe -ErrorAction SilentlyContinue).Source
if (-not $mysql -and (Test-Path 'G:\MySQL\bin\mysql.exe')) { $mysql = 'G:\MySQL\bin\mysql.exe' }
if (-not $mysql) { $mysql = 'mysql.exe' }
$base = @(
    "--host=$dbHost",
    "--port=$dbPort",
    "--user=$dbUser",
    "--password=$env:DB_PASSWORD",
    '--default-character-set=utf8'
)
