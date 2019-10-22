package uk.ac.warwick.camcat;

import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.deployment.Deployment;
import com.atlassian.bamboo.specs.api.builders.deployment.Environment;
import com.atlassian.bamboo.specs.api.builders.notification.Notification;
import com.atlassian.bamboo.specs.api.builders.plan.Job;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.plan.PlanIdentifier;
import com.atlassian.bamboo.specs.api.builders.plan.Stage;
import com.atlassian.bamboo.specs.api.builders.plan.artifact.Artifact;
import com.atlassian.bamboo.specs.api.builders.project.Project;
import com.atlassian.bamboo.specs.api.builders.requirement.Requirement;
import com.atlassian.bamboo.specs.api.util.EntityPropertiesBuilders;
import com.atlassian.bamboo.specs.builders.notification.DeploymentFailedNotification;
import com.atlassian.bamboo.specs.builders.task.*;
import com.atlassian.bamboo.specs.builders.trigger.AfterSuccessfulBuildPlanTrigger;
import com.atlassian.bamboo.specs.model.task.ScriptTaskProperties;
import com.atlassian.bamboo.specs.model.task.TestParserTaskProperties;
import uk.ac.warwick.bamboo.specs.AbstractWarwickBuildSpec;

import java.util.Collection;
import java.util.Collections;

/**
 * Plan configuration for Bamboo.
 * Learn more on: <a href="https://confluence.atlassian.com/display/BAMBOO/Bamboo+Specs">https://confluence.atlassian.com/display/BAMBOO/Bamboo+Specs</a>
 */
@BambooSpec
public class AppPlanSpec extends AbstractWarwickBuildSpec {

  private static final Project PROJECT =
    new Project()
      .key("CAMCAT")
      .name("Course and Module Catalogue");

  private static final String LINKED_REPOSITORY = "Course and Module Catalogue";

  private static final String SLACK_CHANNEL = "#course-module-cat";

  public static void main(String[] args) throws Exception {
    new AppPlanSpec().publish();
  }

  private static Stage buildStage() {
    Job job =
      new Job("Build and check", "BUILD")
        .tasks(
          new VcsCheckoutTask()
            .description("Checkout source from default repository")
            .checkoutItems(new CheckoutItem().defaultRepository()),
          new ScriptTask()
            .description("gradlew clean check bootJar")
            .interpreter(ScriptTaskProperties.Interpreter.BINSH_OR_CMDEXE)
            .location(ScriptTaskProperties.Location.FILE)
            .fileFromPath("gradlew")
            .argument("clean check bootJar --no-daemon")
            .environmentVariables("JAVA_OPTS=\"-Xmx256m -Xms128m\""),
          new ScriptTask()
            .description("Touch test files so Bamboo doesn't ignore them")
            .interpreter(ScriptTaskProperties.Interpreter.BINSH_OR_CMDEXE)
            .location(ScriptTaskProperties.Location.INLINE)
            .inlineBody("find . -type f -name 'TEST-*.xml' -exec touch {} +")
        )
        .requirements(
          new Requirement("system.jdk.JDK 1.8")
        );

    job.finalTasks(
      new TestParserTask(TestParserTaskProperties.TestType.JUNIT)
        .description("Parse test results")
        .resultDirectories("**/test-results/**/*.xml")
    );

    job.artifacts(
      new Artifact()
        .name("app.jar")
        .copyPattern("app.jar")
        .location("build/libs")
        .shared(true)
    );

    return new Stage("Build Stage").jobs(job);
  }

  @Override
  protected Collection<Plan> builds() {
    return Collections.singletonList(
      build(PROJECT, "ALL", "Course and Module Catalogue")
        .linkedRepository(LINKED_REPOSITORY)
        .description("Run checks and build executable jar")
        .stage(buildStage())
        .slackNotifications(SLACK_CHANNEL, false)
        .build()
    );
  }

  @Override
  protected Collection<Deployment> deployments() {
    return Collections.singleton(
      deployment(PROJECT, "ALL", "Course and Module Catalogue")
        .environment(new Environment("Development")
          .description("courses-dev.warwick.ac.uk")
          .tasks(
            new CleanWorkingDirectoryTask(),
            new ArtifactDownloaderTask()
              .description("Download release contents")
              .artifacts(new DownloadItem().allArtifacts(true)),
            new ScpTask()
              .description("Copy bootJar to deployment server")
              .fromArtifact(
                new ArtifactItem()
                  .sourcePlan(new PlanIdentifier(EntityPropertiesBuilders.build(PROJECT).getKey().getKey(), "ALL"))
                  .allArtifacts()
              )
              .host("appdeployment.warwick.ac.uk")
              .toRemotePath("/home/ansible/artifacts/camcat_dev/")
              .username("ansible")
              .authenticateWithKeyWithPassphrase(DEPLOYMENT_KEY, DEPLOYMENT_KEY_PASSPHRASE),
            new SshTask()
              .description("Run Ansible")
              .host("appdeployment.warwick.ac.uk")
              .username("ansible")
              .authenticateWithKeyWithPassphrase(DEPLOYMENT_KEY, DEPLOYMENT_KEY_PASSPHRASE)
              .command(
                "cd ~/appdeployment/gem\r\n" +
                  "bundle exec bin/deploy-springboot camcat dev"
              )
          )
          .triggers(new AfterSuccessfulBuildPlanTrigger())
          .notifications(
            new Notification()
              .type(new DeploymentFailedNotification())
              .recipients(slackRecipient(SLACK_CHANNEL))
          )
        )
        .build()
    );
  }

  private static final String DEPLOYMENT_KEY =
    "-----BEGIN RSA PRIVATE KEY-----\n" +
      "Proc-Type: 4,ENCRYPTED\n" +
      "DEK-Info: AES-128-CBC,5872F24CD90524DF88C22A4AB949B382\n" +
      "\n" +
      "lYzChoPlRYEOPnDmyWmwj+hwulgOuoB6NoOUrOs2TcBbqaWH1rYB0v64vuA6rz5V\n" +
      "6b/LQWx0FwnJk0oJbFxZygF8lN1eqmTpheYIkYMFvNHlyEmzI0FsDEwNzJN5gOKe\n" +
      "xNbs/BJPtbjgP5yDc5scLMX5+XMZsNnTV0Pm6CDot9SLmdEKK7FUGaTuvDPNSGdz\n" +
      "eYL5HQ45KdwpVDXSTMBIge3xycPkNZyVHPetMGBrupL0kentKC97+rOTVSJ+vPKZ\n" +
      "+KKK/60xNdQdPW8Xn3CwQ1Z8xd9qt64nD3az6NqJTPn1f3FNebJXtIYXXaIBZAt6\n" +
      "S/WUHrPfI4Ys6zrUDHfqezC0v64rklo8QG8XRdAv6h6b5SRLHlW6NoH5kVULhLnw\n" +
      "bETt4mOAisiZMEw89KpZbfgewk+pJVa0Pkg0nCTQsKAuCnKBgyyCm4Im2GScAp9O\n" +
      "Cad+8ejlB0NkrXNJR1gx5zJ8w2cjFRxC7AlMZO+njQobB0+g3m2O36XjyQm9rw87\n" +
      "dNfj93bmYuQuJAg0YpaX8KzlmXRlb4HlP+1HyApPjP0G42ceRevJ3N5oqupCtGhN\n" +
      "o7shTVszpmKHt+me+ZpvxR8rM8qSBIm4J8mJeCeKV5Yib0kx3ScqRkSuGvEWVuzO\n" +
      "tOMA/xPRLuT8dyYNldMf3FQ/mCoe/pcyr4VWxGZkcVOgsV/teNcgvr7KdJz2VEMw\n" +
      "TxKuf9M/7uxaLZbh/bHkWKTYCh4pMENlPkCb/9xilUG/0lmCrJAkIIlQk7r8I6Tl\n" +
      "lHi71aDvZX5DAWwc8HQGURgZoM/+zA9jZj7CyPgqiZbkkvqEywQccUT4kNO0NU/C\n" +
      "/ACKkw8XTCBQcjNPB+kGll2WEuo+xSKaY0FPnr44rJCMyFua2E9+FPyOgF3MdhwD\n" +
      "N4NfJpG7xd3eJiACHOOn4BfaOpnyv+njCAL9DsYkcikbUk2qJd6qAn9rL4M7AsBe\n" +
      "xjltw0qIWyjKY3oYGtzNT79B6rxEBQUJT5vPp1RQ6jaeI4vA1r+gYt+bLThTP6FE\n" +
      "/d2Ovevq2qzDLthjiId7eEzt6QnSA6X7qatw+Y7Rq4/vsKQMSdXc6F3u/cqXH7Lg\n" +
      "1xjnsZhZn+TDMo8sLywIOnr74yfQgKxmgAgBVzzsfjcztH57wuD4rHw9jicXD36C\n" +
      "TbupgvsOrh3xf3/UVwV3cV7kgWEAT64zDb4FTiEW5e/S4LV5tXciZoX58Z77E4Uw\n" +
      "ZI91kzEL3wy0KSnE8CHaGl7eyz16H4ysbTKLQKCPkBvDJDOxEJqFJQVIXY829FJM\n" +
      "kCJSGN58UpBrEjD65wG0GgX/JjU9XsAufDQiDAb6VKus12LglJterdLmUv0IDbzh\n" +
      "IUtNLtuvibAEA/HbSehs9UKF5KmVbX6+0jOTenu8mOYVx2rH57Q6v97lQhHGuBhi\n" +
      "SaCTz3NciQrnEkSgY1rzCqdrhwYdegqSX/DjYOhHutfE6qlYD0xsv/2vt/aXkzRX\n" +
      "s90yaNYNomIgPGNo60DjSHZ0lXI5rRiEAcmsVLxZ/QwTv7Cf1xFi838ohxXXvphu\n" +
      "YkpT2jf/V6iL1I+HLM9G19ZWMi0WyFIGbZu2VBn48Ml4fbzpT1cCzYiSLB7eTbuV\n" +
      "-----END RSA PRIVATE KEY-----";

  private static final String DEPLOYMENT_KEY_PASSPHRASE = "elabelab";

}
