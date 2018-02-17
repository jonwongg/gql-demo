package entities.throwables

import sangria.execution.UserFacingError
import sangria.schema.Argument

class NotFoundThrowable(`type`: String, id: String) extends Throwable(s"${`type`}: $id does not exist") with UserFacingError