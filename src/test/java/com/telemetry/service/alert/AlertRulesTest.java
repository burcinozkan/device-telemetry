package com.telemetry.service.alert;

import com.telemetry.domain.DeviceStatus;
import com.telemetry.domain.EventType;
import com.telemetry.domain.TelemetryEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class AlertRulesTest {

    @Test
    void highPowerConsumptionRuleTriggersAboveThreshold() {
        HighPowerConsumptionRule rule = new HighPowerConsumptionRule(2500);

        var trigger = rule.evaluate(event(3000.0, null, null, DeviceStatus.RUNNING, false, null));

        assertThat(trigger).isPresent();
        assertThat(trigger.get().ruleName()).isEqualTo("HIGH_POWER_CONSUMPTION");
    }

    @Test
    void doorOpenWhileRunningRuleTriggersForRunningDevice() {
        DoorOpenWhileRunningRule rule = new DoorOpenWhileRunningRule();

        var trigger = rule.evaluate(event(100.0, null, null, DeviceStatus.RUNNING, true, null));

        assertThat(trigger).isPresent();
        assertThat(trigger.get().severity().name()).isEqualTo("CRITICAL");
    }

    @Test
    void overTemperatureRuleDoesNotTriggerBelowThreshold() {
        OverTemperatureRule rule = new OverTemperatureRule(90);

        var trigger = rule.evaluate(event(null, 45.0, null, DeviceStatus.RUNNING, false, null));

        assertThat(trigger).isEmpty();
    }

    @Test
    void deviceErrorReportRuleTriggersForErrorEvent() {
        DeviceErrorReportRule rule = new DeviceErrorReportRule();

        var event = TelemetryEvent.builder()
                .deviceId("WM-001")
                .eventType(EventType.ERROR_REPORT)
                .eventTimestamp(Instant.now())
                .errorCode("E-102")
                .build();

        assertThat(rule.evaluate(event)).isPresent();
    }

    @Test
    void excessiveWaterUsageRuleTriggersAboveThreshold() {
        ExcessiveWaterUsageRule rule = new ExcessiveWaterUsageRule(80);

        var trigger = rule.evaluate(event(null, null, 95.0, DeviceStatus.RUNNING, false, null));

        assertThat(trigger).isPresent();
    }

    private TelemetryEvent event(
            Double power,
            Double temperature,
            Double water,
            DeviceStatus status,
            boolean doorOpen,
            String errorCode) {
        return TelemetryEvent.builder()
                .deviceId("WM-001")
                .eventType(EventType.STATUS_UPDATE)
                .eventTimestamp(Instant.now())
                .deviceStatus(status)
                .powerWatts(power)
                .temperatureCelsius(temperature)
                .waterLiters(water)
                .doorOpen(doorOpen)
                .errorCode(errorCode)
                .build();
    }
}
