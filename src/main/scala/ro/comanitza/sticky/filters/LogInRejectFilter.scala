package ro.comanitza.sticky.filters

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import javax.servlet.{Filter, FilterChain, ServletRequest, ServletResponse}
import org.springframework.http.HttpStatus
import ro.comanitza.sticky.Constants.{LOGGED_IN, USER_ID}

class LogInRejectFilter extends Filter {

  override def doFilter(req: ServletRequest, rsp: ServletResponse, filterChain: FilterChain): Unit = {

    if (isUserLoggedIn(req)) {
      filterChain.doFilter(req, rsp)
    } else {
      rsp.asInstanceOf[HttpServletResponse].sendError(HttpStatus.UNAUTHORIZED.value())
    }
  }

  protected def isUserLoggedIn(req: ServletRequest): Boolean = {

    val session = req.asInstanceOf[HttpServletRequest].getSession

    if (session == null) {
      return false
    }

    if (session.getAttribute(USER_ID) == null) {
      return false
    }

    session.getAttribute(LOGGED_IN).asInstanceOf[Boolean]
  }
}
