package tw.yen.spring.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="user_info")
public class UserInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name="account")
	private String uAccount;
	@Column(name="password")
	private String uPassword;
//	@Column(name="name")
//	private String uName;
	@Column(name="email")
	private String uEmail;
	@Column(name="role")
	private Integer role;   // 職務權限設定
	@Column(name="status")
	private Integer status;
	@Column(name="company_id" , insertable = false, updatable = false)  // 唯讀，以免hibernate報錯
	private Long cId;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getuAccount() {
		return uAccount;
	}
	public void setuAccount(String uAccount) {
		this.uAccount = uAccount;
	}
	public String getuPassword() {
		return uPassword;
	}
	public void setuPassword(String uPassword) {
		this.uPassword = uPassword;
	}
/*	
 * public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
*/
	public String getuEmail() {
		return uEmail;
	}
	public void setuEmail(String uEmail) {
		this.uEmail = uEmail;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getcId() {
		return cId;
	}
	public void setcId(Long cId) {
		this.cId = cId;
	}
	
	
	// 多對一
	@ManyToOne
	@JoinColumn(name="company_id", nullable = false)
	@JsonBackReference
	private CompanyInfo companyId;


	public CompanyInfo getCompanyId() {
		return companyId;
	}
	public void setCompanyId(CompanyInfo companyId) {
		this.companyId = companyId;
	}
	
}
