package ro.comanitza.sticky.filters

import javax.servlet.http.{HttpServletRequest, HttpServletResponse, HttpSession}
import javax.servlet.{Filter, FilterChain, ServletRequest, ServletResponse}
import ro.comanitza.sticky.Constants.{ADMIN, LOGGED_IN, USER_ID}

class AdminFilter extends Filter {

  override def doFilter(req: ServletRequest, rsp: ServletResponse, filterChain: FilterChain): Unit = {

    if (isAdmin(req: ServletRequest)) {
      filterChain.doFilter(req, rsp)
    } else {
      rsp.asInstanceOf[HttpServletResponse].sendRedirect("/login")
    }
  }

  private def isAdmin(req: ServletRequest): Boolean = {

    val session = req.asInstanceOf[HttpServletRequest].getSession

    if (session == null) {
      return false
    }

    if (session.getAttribute(ADMIN) == null) {
      return false
    }

    session.getAttribute(ADMIN).asInstanceOf[Boolean]
  }
}
