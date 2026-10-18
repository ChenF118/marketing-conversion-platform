# Start MySQL + backend + frontend in two windows, then open the browser
# Usage: powershell -ExecutionPolicy Bypass -File start-all.ps1
$root = $PSScriptRoot

Start-Process powershell -ArgumentList @(
    '-NoExit', '-NoProfile', '-ExecutionPolicy', 'Bypass',
    '-File', (Join-Path $root 'start-backend.ps1')
)

Write-Host '[...] waiting for backend on port 8000'
$ready = $false
for ($i = 0; $i -lt 90; $i++) {
    if (Test-NetConnection -ComputerName 127.0.0.1 -Port 8000 -InformationLevel Quiet -WarningAction SilentlyContinue) {
        $ready = $true
        break
    }
    Start-Sleep -Seconds 1
}
if (-not $ready) { Write-Host '[WARN] backend not ready after 90s, check the backend window' }

Start-Process powershell -ArgumentList @(
    '-NoExit', '-NoProfile', '-ExecutionPolicy', 'Bypass',
    '-File', (Join-Path $root 'start-frontend.ps1')
)

Write-Host '[...] waiting for frontend on port 8080'
for ($i = 0; $i -lt 90; $i++) {
    if (Test-NetConnection -ComputerName 127.0.0.1 -Port 8080 -InformationLevel Quiet -WarningAction SilentlyContinue) {
        break
    }
    Start-Sleep -Seconds 1
}

Start-Process 'http://localhost:8080'
Write-Host '[OK] backend  http://127.0.0.1:8000'
Write-Host '[OK] frontend http://localhost:8080  (login: admin / 123456)'
