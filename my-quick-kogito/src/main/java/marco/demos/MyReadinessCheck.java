package marco.demos;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
public class MyReadinessCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        System.out.println("--> ready");
        return HealthCheckResponse.up("ready");
    }

}