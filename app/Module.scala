import com.google.inject.AbstractModule
import play.api.{Configuration, Environment, Logger}

import scala.concurrent.ExecutionContext.Implicits.global

class Module(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure = {}
}
