# populate-demo.ps1
# demo telemetry generator
$base = "http://localhost:8090"

Write-Host "Creating devices..."

$devices=@(
@{deviceId="wm-001";name="Kitchen Washer";type="WASHING_MACHINE";ownerId="user-1";location="Kitchen"},
@{deviceId="dw-001";name="Dishwasher";type="DISHWASHER";ownerId="user-1";location="Kitchen"},
@{deviceId="dryer-001";name="Dryer";type="DRYER";ownerId="user-1";location="Laundry"}
)

foreach($d in $devices){
    try{
        Invoke-RestMethod -Method POST -Uri "$base/api/devices" -ContentType "application/json" -Body ($d|ConvertTo-Json)|Out-Null
    }catch{}
}

function Send-Event($body){
    Invoke-RestMethod -Method POST `
        -Uri "$base/api/telemetry/events" `
        -ContentType "application/json" `
        -Body ($body|ConvertTo-Json)|Out-Null
}

$start=(Get-Date).AddDays(-7)

foreach($dev in @(
    @{id="wm-001";power=1450;temp=48;water=35;phase=@("FILL","WASH","RINSE","SPIN")},
    @{id="dw-001";power=1150;temp=56;water=22;phase=@("PREWASH","WASH","DRY")},
    @{id="dryer-001";power=1800;temp=67;water=0;phase=@("HEATING","DRYING","COOLING")}
)){
    for($i=0;$i -lt 700;$i++){

        $t=$start.AddMinutes($i*15 + (Get-Random -Minimum -4 -Maximum 5))

        $p=$dev.power + (Get-Random -Minimum -120 -Maximum 121)
        $tmp=$dev.temp + (Get-Random -Minimum -4 -Maximum 5)
        $w=[Math]::Max(0,$dev.water + (Get-Random -Minimum -3 -Maximum 4))

        if((Get-Random -Maximum 100)-lt 3){
            $p += Get-Random -Minimum 900 -Maximum 1700
        }

        if((Get-Random -Maximum 100)-lt 2){
            $tmp += Get-Random -Minimum 18 -Maximum 35
        }

        $status="RUNNING"
        $etype="STATUS_UPDATE"
        $err=$null

        if((Get-Random -Maximum 1000)-lt 6){
            $etype="ERROR_REPORT"
            $status="ERROR"
            $err=("E-"+(Get-Random -Minimum 100 -Maximum 999))
            $p=0
        }

        $body=@{
            deviceId=$dev.id
            eventType=$etype
            timestamp=$t.ToString("o")
            deviceStatus=$status
            powerWatts=$p
            temperatureCelsius=$tmp
            waterLiters=$w
            programPhase=$dev.phase[(Get-Random -Maximum $dev.phase.Count)]
            doorOpen=((Get-Random -Maximum 100)-lt 1)
            errorCode=$err
        }

        Send-Event $body
    }
}

Write-Host ""
Write-Host "Finished generating realistic telemetry."
