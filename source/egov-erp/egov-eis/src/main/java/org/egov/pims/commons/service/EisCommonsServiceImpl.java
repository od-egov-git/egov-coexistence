/*
 *	@(#)GenericCommonsManagerBean.java		Oct 25, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * eGov PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.commons.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.utils.EGovConfig;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.dao.PositionMasterDAO;
import org.egov.pims.dao.PersonalInformationHibernateDAO;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.utils.EisManagersUtill;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @author Venkatesh.M.J
 * @version 1.2
 * @since 1.2
 */
@Service("eisCommonsService")
public class EisCommonsServiceImpl implements EisCommonsService {


	private static final Logger logger = Logger.getLogger(EisCommonsServiceImpl.class);
	@Autowired
	private UserService userService;
	
	@Autowired
	private PositionMasterDAO positionMasterDAO;
	
	@Autowired
	private PersonalInformationHibernateDAO pimsDao;
    
    @PersistenceContext
	private EntityManager entityManager;
    
	public Session  getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	public  void updatePosition(Position position)
	{
		try
		{
			positionMasterDAO.updatePosition(position);
		}
		catch(Exception e)
		{
			
			throw new EGOVRuntimeException("Exception in deleting Installment."+e.getMessage(),e);
		}

	}
	public  Position getPositionById(Integer positionId)

	{
		Position pos = null;
		logger.info("InsidegetPositionById :  positionId="+positionId);
		try
		{
			if(positionId != null)
			{
				pos = positionMasterDAO.getPosition(positionId);
			}
			return pos;
		}
		catch(Exception e)
		{
			
			throw new EGOVRuntimeException("Exception in deleting Installment."+e.getMessage(),e);
		}

	}

    public  Position getPositionByUserId(Integer userId)
    {

		Position userPosition = null;
		
		Date currentDate = new Date();
		try
		{

			String mainStr = "";
			mainStr = " select POS_ID from EG_EIS_EMPLOYEEINFO ev where ev.USER_ID = :userid and ((ev.to_Date is null and ev.from_Date <= :thisDate ) " +
					" OR (ev.from_Date <= :thisDate AND ev.to_Date >= :thisDate)) and ev.IS_PRIMARY ='Y'";
			Query qry = getCurrentSession().createSQLQuery(mainStr).addScalar("POS_ID", IntegerType.INSTANCE);
			qry.setInteger ("userid", userId);
			qry.setDate("thisDate", currentDate);
			List retList = qry.list();
			if(retList!=null && !retList.isEmpty())
			{
				Integer posId = null;
				for(Iterator iter = retList.iterator();iter.hasNext();)
				{
					posId = (Integer)iter.next();
				}
				if (posId != null)
				{
					userPosition = getPositionById(posId);
				}
			}
		}
		catch (HibernateException he) {
				
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			} catch (Exception he)
			{
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			}
			return userPosition;
 
    }

	public Position getPositionForUserByIdAndDate(Integer userId, Date assignDate)
	{
		Position userPosition = null;
		//PersonalInformation personalInformation = new PersonalInformation();
		try
		{

			String mainStr = "";
			mainStr = " select POS_ID from EG_EIS_EMPLOYEEINFO ev where ev.USER_ID = :userid and ((ev.to_Date is null and ev.from_Date <= :thisDate ) OR (ev.from_Date <= :thisDate AND ev.to_Date > :thisDate))";
			Query qry = getCurrentSession().createSQLQuery(mainStr).addScalar("POS_ID", IntegerType.INSTANCE);
			qry.setInteger ("userid", userId);
			qry.setDate("thisDate", assignDate);
			List retList = qry.list();
			if(retList!=null && !retList.isEmpty())
			{
				Integer posId = null;
				for(Iterator iter = retList.iterator();iter.hasNext();)
				{
					posId = (Integer)iter.next();
				}
				if (posId != null)
				{
					userPosition = getPositionById(posId);
				}
			}
		}
		catch (HibernateException he) {
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			} catch (Exception he)
			{
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			}
			return userPosition;

	}

	public User getUserforPosition(Position pos)
	{
		User uerImpl= null;
		
		try
		{
			
		    String mainStr = "";
			mainStr = " select 	USER_ID  from EG_EIS_EMPLOYEEINFO ev  where ev.POS_ID = :pos and ((ev.to_Date is null and ev.from_Date <= SYSDATE ) OR (ev.from_Date <= SYSDATE AND ev.to_Date > SYSDATE))";
			Query qry = getCurrentSession().createSQLQuery(mainStr).addScalar("USER_ID", IntegerType.INSTANCE);

			if(pos != null)
			{
				qry.setEntity("pos", pos);
			}
			if(qry.list()!=null&&!qry.list().isEmpty())
			{
				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
				    Long userId = (Long)iter.next();
					uerImpl = userService.getUserById(userId);
				}
			}
		}
		catch (HibernateException he) {
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			} catch (Exception he)
			{
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			}
			return uerImpl;

	}
	
	public Boolean isEmployeeAutoGenerateCodeYesOrNo()
	{
		String employeeAutoGenCodeYesOrNo=EGovConfig.getAppConfigValue("Employee","EMPAUTOGENERATECODE","no");
		boolean autoGenCode = false;
		if("yes".equalsIgnoreCase(employeeAutoGenCodeYesOrNo))
		{
			autoGenCode=true;
		}
		
		return autoGenCode;
	}
	 public Boolean checkEmpCode(String empCode)
	 {
		 boolean checkEmpCode = false;
		 Query qry = null;
		 
		 try
		 {
			String main="from PersonalInformation where employeeCode=:employeeCode";
			qry=getCurrentSession().createQuery(main);
			qry.setString("employeeCode", empCode);
			if(qry.list()!=null && !qry.list().isEmpty())
			{
				checkEmpCode = true;
			}
		 }
		 catch (HibernateException he) {
				
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			} catch (Exception he)
			{
				
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			}
		 return checkEmpCode;
	 }
	 
	 public  Position getPositionByName(String positionName){
		 
		    
			Query qry = null;
			try
			 {
				String main= "from Position where name=:positionName";
				qry=getCurrentSession().createQuery(main);
				if(positionName!=null && !positionName.equals(""))
				{
					qry.setString("positionName", positionName);
				}
				
				return (Position)qry.uniqueResult();
				
			 }
			 catch (HibernateException he) {
					throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
				} catch (Exception he)
				{
					throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
				}
				
		 }
		
	 /**
		 * This method returns the current position id  of the user
		 * 
		 * @param user the user whose designation is needed.
		 * 
		 * 
		 * @return the position id as integer 
		 */
	public Position getCurrentPositionByUser(User user)
	{
		Position position = null;
		try{
		if (null != user){
			PersonalInformation personalInfo = EisManagersUtill.getEmployeeService().getEmpForUserId(user.getId());
			position = EisManagersUtill.getEmployeeService().getPositionforEmp(personalInfo.getIdPersonalInformation());
			
		}
		}catch(Exception e)
		{
			
			throw new EGOVRuntimeException("Exception in getCurrentPositionByUser :"+e.getMessage(),e);
		}
		return position;
	}
	
	public  User getUserForPosition(Integer posId, Date date)
	{
		User user = null;
		
		try
		{
			
			
			String mainStr = "";
			mainStr = " select USER_ID from EG_EIS_EMPLOYEEINFO ev where ev.pos_id = :posId and ((ev.to_Date is null and ev.from_Date <= :thisDate ) OR (ev.from_Date <= :thisDate AND ev.to_Date > :thisDate))";
			Query qry = getCurrentSession().createSQLQuery(mainStr).addScalar("USER_ID", IntegerType.INSTANCE);
			qry.setInteger ("posId", posId);
			qry.setDate("thisDate", date);
			List retList = qry.list();
			if(retList!=null && !retList.isEmpty())
			{
			    Long userId = null;
				for(Iterator iter = retList.iterator();iter.hasNext();)
				{
					userId = (Long)iter.next();
				}
				if (userId != null)
				{
					user = userService.getUserById(userId);
				}
			}
		}
		catch (HibernateException he) {
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			} catch (Exception he)
			{
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			}
			return user;
		
	}
	/**
	 * Api to get unique designation based on dept and functionary
	 * @param deptId
	 * @param functionaryId
	 * @return unique designation from view if dept and functionary is 0
	 * else based on dept and functionary
	 * @throws Exception
	 */
	 public List<DesignationMaster> getDesigantionBasedOnFuncDept(Integer deptId,Integer functionaryId) throws Exception
		{
		 	
			
			List<EmployeeView> employeeList = null;
			List<DesignationMaster> desgMstr = new ArrayList<DesignationMaster>();
			try
			{				
				String mainStr = "";
				String subQry = " from EmployeeView ev ";
				if( ((deptId!=null && deptId!=0) && functionaryId==0 ))
				{
					subQry+=" where ev.deptId = :deptId ";
				}
				else if(( (functionaryId!=null && functionaryId!=0) && deptId==0))
				{
					subQry+= " where ev.functionary =:functionaryId";
				}
				else if(deptId!=null && deptId!=0 && functionaryId!=null && functionaryId!=0 )
				{
					subQry+= " where ev.deptId = :deptId and ev.functionary =:functionaryId";
				}
				
				subQry=	"select distinct ev.desigId.designationId "+subQry;
				mainStr ="from DesignationMaster dm   where dm.designationId in( "+subQry+"  ) "; 
					
				Query query = getCurrentSession().createQuery(mainStr);
				if(deptId!=null && deptId!=0)
				{
					query.setInteger("deptId", deptId);
				}
				
				if(functionaryId!=null && functionaryId!=0)
				{
					query.setInteger("functionaryId", functionaryId);
				}
				
				desgMstr=(List<DesignationMaster>)query.list();
				
			}
			catch(Exception e){
				
				throw new EGOVRuntimeException(e.getMessage(),e);
			}
			return desgMstr;
			
		}
	 
	 
	 
	/**
	  * Returning temporary  assigned employee object by department,designation,functionary,date 
	  * @param deptId
	  * @param DesigId
	  * @param functionaryId
	  * @param onDate
	  * @return Employee
	  * @throws Exception 
	  */
	 public PersonalInformation getTempAssignedEmployeeByDeptDesigFunctionaryDate(Integer deptId, Integer desigId, Integer functionaryId, Date onDate) throws Exception{
		 return pimsDao.getTempAssignedEmployeeByDeptDesigFunctionaryDate(deptId, desigId, functionaryId, onDate);
	 }
	 
	 private final static String STR_EXCEPTION="Exception:";

}
