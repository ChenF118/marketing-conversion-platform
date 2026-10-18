# Stop backend (8000) and frontend (8080)
# Usage: powershell -ExecutionPolicy Bypass -File stop-all.ps1 [-Ports 8000,8080]
param([int[]]$Ports = @(8000, 8080))

foreach ($p in $Ports) {
    $conns = Get-NetTCPConnection -LocalPort $p -State Listen -ErrorAction SilentlyContinue
    if (-not $conns) { Write-Host "[--] port $p not listening"; continue }
    foreach ($c in $conns) {
        try {
            Stop-Process -Id $c.OwningProcess -Force
            Write-Host "[OK] stopped port $p (pid $($c.OwningProcess))"
        } catch {
            Write-Host "[WARN] cannot stop port $p (pid $($c.OwningProcess)): $($_.Exception.Message)"
        }
    }
}
