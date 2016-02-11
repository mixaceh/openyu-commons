package org.openyu.commons.web.struts2.supporter;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.CookiesAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.context.ApplicationContext;

import org.openyu.commons.bean.BeanCollector;
import org.openyu.commons.bean.TrueFalseOption;
import org.openyu.commons.bean.WhetherOption;
import org.openyu.commons.dao.inquiry.Inquiry;
import org.openyu.commons.dao.inquiry.Order;
import org.openyu.commons.dao.inquiry.Pagination;
import org.openyu.commons.dao.inquiry.Sort;
import org.openyu.commons.dao.inquiry.impl.InquiryImpl;
import org.openyu.commons.dao.inquiry.impl.OrderImpl;
import org.openyu.commons.dao.inquiry.impl.SortImpl;
import org.openyu.commons.lang.BooleanHelper;
import org.openyu.commons.lang.CharHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.lang.StringHelper;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.thread.ThreadService;
import org.openyu.commons.thread.anno.DefaultThreadService;
import org.openyu.commons.util.CollectionHelper;
import org.openyu.commons.util.DateHelper;
import org.openyu.commons.util.TimeZoneHelper;
import org.openyu.commons.web.struts2.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * 控制器
 * 
 * SUCCESS = "success"
 * 
 * NONE = "none"
 * 
 * ERROR = "error"
 * 
 * INPUT = "input"
 * 
 * LOGIN = "login"
 */
public class BaseActionSupporter extends ActionSupport implements BaseAction, Supporter, ServletContextAware,
		SessionAware, ServletRequestAware, ServletResponseAware, CookiesAware, Preparable {
	private static final long serialVersionUID = -7712187070058585941L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BaseActionSupporter.class);

	protected transient ApplicationContext applicationContext;

	@DefaultThreadService
	protected transient ThreadService threadService;

	/**
	 * 靜態收集器
	 */
	protected transient BeanCollector beanCollector = BeanCollector.getInstance();

	/**
	 * application
	 */
	protected ServletContext application;

	/**
	 * session
	 */
	protected Map<String, Object> session;

	/**
	 * httpSession
	 */
	protected HttpSession httpSession;

	/**
	 * request
	 */
	protected HttpServletRequest request;

	/**
	 * reponse
	 */
	protected HttpServletResponse reponse;

	/**
	 * cookiesMap
	 */
	protected Map<String, String> cookiesMap;

	/**
	 * cookies
	 */
	protected Cookie[] cookies;

	/**
	 * pageContext
	 */
	protected PageContext pageContext;

	/**
	 * 查詢條件
	 */
	protected Inquiry inquiry = new InquiryImpl();

	/**
	 * db pk
	 * 
	 * for pick 放pk,checkbox用
	 * 
	 * request丟進來時是字串
	 */
	protected List<Serializable> seqs = new LinkedList<Serializable>();

	// --------------------------------------------------
	// 上傳,須配合FileUploadInterceptor
	// --------------------------------------------------
	/**
	 * 上傳檔案的標題
	 */
	protected String[] uploadTitle;

	/**
	 * 上傳檔案
	 */
	protected File[] upload;

	/**
	 * 上傳檔案名稱
	 */
	protected String[] uploadFileName;

	/**
	 * 上傳檔案的類型
	 */
	protected String[] uploadContentType;

	/**
	 * 上傳檔案的儲存絕對路徑
	 */
	protected String savePath;

	// --------------------------------------------------
	// 下載
	// --------------------------------------------------
	protected InputStream downloadStream;

	public BaseActionSupporter() {
	}

	/**
	 * struts2 作為初始化用
	 */
	public void prepare() throws Exception {
		initialize();
	}

	/**
	 * 初始化
	 * 
	 * 在action 上,利用 implements Preparable, 及struts.xml: Prepare Interceptor
	 * <interceptor-ref name="prepare" />,初始化
	 */
	public void initialize() {
		// 初始化查詢條件
		initializeInquiry(beanCollector.createInquiry());

		// 目前排序欄位
		inquiry.setSort(new SortImpl());
		// 目前排序方向
		inquiry.setOrder(new OrderImpl());
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public ServletContext getApplication() {
		return application;
	}

	public void setServletContext(ServletContext application) {
		this.application = application;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public String getSessionId() {
		return httpSession.getId();
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		//
		this.httpSession = request.getSession();
		this.cookies = request.getCookies();
	}

	public HttpServletResponse getReponse() {
		return reponse;
	}

	public void setServletResponse(HttpServletResponse reponse) {
		this.reponse = reponse;
	}

	public Map<String, String> getCookiesMap() {
		return cookiesMap;
	}

	public void setCookiesMap(Map<String, String> cookiesMap) {
		this.cookiesMap = cookiesMap;
	}

	public Cookie[] getCookies() {
		return cookies;
	}

	public PageContext getPageContext() {
		// return (PageContext)
		// ActionContext.getContext().get(ServletActionContext.PAGE_CONTEXT);
		return ServletActionContext.getPageContext();
	}

	/**
	 * 使用者session區域
	 * 
	 * @return
	 */
	public Locale getLocale() {
		return super.getLocale();
	}

	/**
	 * 使用者session時區
	 * 
	 * @return
	 */
	public TimeZone getTimeZone() {
		// TODO 應改為session上的timeZone,暫時先用系統的
		return TimeZoneHelper.getTimeZone();
	}

	/**
	 * 是否(true/false)選項
	 * 
	 * @return
	 */
	public List<TrueFalseOption> getTrueFalseOptions() {
		return beanCollector.getTrueFalseOptions();
	}

	public String getTrueFalseName(boolean value) {
		return getTrueFalseName(value, getLocale());
	}

	/**
	 * 取得,是否(true/false)選項名稱
	 * 
	 * @param value,
	 *            TrueFalse.getId().getValue()
	 * @param locale
	 * @return
	 */
	public String getTrueFalseName(boolean value, Locale locale) {
		return beanCollector.getTrueFalseName(value, locale);
	}

	/**
	 * 全部是否("all"/"true"/"false")選項
	 * 
	 * @return
	 */
	public List<WhetherOption> getWhetherOptions() {
		return beanCollector.getWhetherOptions();
	}

	public String getWhetherName(String value) {
		return getWhetherName(value, getLocale());
	}

	/**
	 * 取得,全部是否("all"/"true"/"false")選項名稱
	 * 
	 * @param value,
	 *            Whether.getId().getValue()
	 * @param locale
	 * @return
	 */
	public String getWhetherName(String value, Locale locale) {
		return beanCollector.getWhetherName(value, locale);
	}

	/**
	 * 查詢條件
	 * 
	 * @return
	 */
	public Inquiry getInquiry() {
		return inquiry;
	}

	public void setInquiry(Inquiry inquiry) {
		this.inquiry = inquiry;
	}

	// pick
	public List<Serializable> getSeqs() {
		return seqs;
	}

	public void setSeqs(List<Serializable> seqs) {
		this.seqs = seqs;
	}

	// --------------------------------------------------
	// 上傳
	// --------------------------------------------------
	public String[] getUploadTitle() {
		return uploadTitle;
	}

	public void setUploadTitle(String[] uploadTitle) {
		this.uploadTitle = uploadTitle;
	}

	public File[] getUpload() {
		return upload;
	}

	public void setUpload(File[] upload) {
		this.upload = upload;
	}

	public String[] getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String[] uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String[] getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String[] uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	// --------------------------------------------------
	// 下載
	// --------------------------------------------------
	public InputStream getDownloadStream() {
		return downloadStream;
	}

	public void setDownloadStream(InputStream downloadStream) {
		this.downloadStream = downloadStream;
	}

	/**
	 * 初始化查詢條件
	 * 
	 * @param value
	 */
	protected void initializeInquiry(Inquiry value) {
		if (value != null) {
			// 分頁條件
			Pagination pagination = value.getPagination();
			if (pagination != null) {
				this.inquiry.setPagination(pagination);
			}
			// 排序欄位選項
			List<Sort> sorts = value.getSorts();
			if (CollectionHelper.notEmpty(sorts)) {
				this.inquiry.setSorts(sorts);
			}
			// 排序方向選項
			List<Order> orders = value.getOrders();
			if (CollectionHelper.notEmpty(orders)) {
				this.inquiry.setOrders(orders);
			}
		}
		// null則清空
		else {
			this.inquiry.setPagination(null);
			this.inquiry.getSorts().clear();
			this.inquiry.getOrders().clear();
		}
	}

	/**
	 * 無指定method時,預設會執行execute方法
	 * 
	 * @return
	 */
	public String execute() {
		return SUCCESS;
	}

	/**
	 * TODO 暫時先這樣,不同apserver可能會導致不同結果
	 * 
	 * @return
	 */
	public String getRealPath() {
		return getRealPath("");
	}

	/**
	 * TODO 暫時先這樣,不同apserver可能會導致不同結果
	 * 
	 * @param path
	 * @return
	 */
	public String getRealPath(String path) {
		return application.getRealPath(path);
	}

	/**
	 * 取得客戶端ip
	 * 
	 * @return
	 */
	public String getClientIp() {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringHelper.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringHelper.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringHelper.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringHelper.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringHelper.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 客戶端port
	 * 
	 * @return
	 */
	public int getClientPort() {
		return request.getRemotePort();
	}

	/**
	 * 伺服器ip
	 * 
	 * @return
	 */
	public String getServerIp() {
		return request.getLocalAddr();
	}

	/**
	 * 伺服器port
	 * 
	 * @return
	 */
	public int getServerPort() {
		return request.getLocalPort();
	}

	// ----------------------------------------------------------------
	// 只是為了簡化寫法
	// ----------------------------------------------------------------

	protected String toString(boolean value) {
		return BooleanHelper.toString(value);
	}

	protected String toString(char value) {
		return CharHelper.toString(value);
	}

	protected String toString(String value) {
		return value;
	}

	// ----------------------------------------------------------------
	// 數字
	// ----------------------------------------------------------------
	protected <T> String toString(Number value) {
		return toString(value, null);
	}

	protected <T> String toString(Number value, String pattern) {
		return toString(value, pattern, getLocale());
	}

	protected <T> String toString(Number value, String pattern, Locale locale) {

		return NumberHelper.toString(value, pattern, locale);
	}

	// ----------------------------------------------------------------
	// 日期
	// ----------------------------------------------------------------
	protected String toString(Date value) {
		return toString(value, null);
	}

	protected String toString(Date value, String pattern) {
		return toString(value, pattern, getTimeZone());
	}

	protected String toString(Date value, String pattern, TimeZone timeZone) {
		return toString(value, pattern, timeZone, getLocale());
	}

	protected String toString(Date value, String pattern, TimeZone timeZone, Locale locale) {
		return DateHelper.toString(value, pattern, timeZone, locale);
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("inquiry", inquiry);
		builder.append("seqs", seqs);
		return builder.toString();
	}

}
