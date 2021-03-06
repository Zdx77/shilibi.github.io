package com.zx.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.zx.dao.IGuestAccountDao;
import com.zx.po.Guest;
import com.zx.util.Md5Utils;

public class GuestAccountDaoImpl implements IGuestAccountDao {

	private SessionFactory sf ;

	@Override
	public Guest GuestLogin(String guestPhone,String guestPwd) {
		Configuration config = new Configuration().configure();
		ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
		sf = new Configuration().configure().buildSessionFactory(sr);
		String hql = " from Guest where guestPhone = :guestPhone  and guestPwd = MD5(:guestPwd)";
		Session session = sf.openSession();
		Query query = session.createQuery(hql).setString("guestPhone", guestPhone).setString("guestPwd", guestPwd);
		Guest g = null;
		g = (Guest) query.uniqueResult();
		session.close();
		return g;
		
	}
	
	
	@Override
	public boolean GuestRegister(Guest guest) {
			guest.setGuestPwd(Md5Utils.md5(guest.getGuestPwd()));
			Configuration configuration=new Configuration().configure();  
	        //创建SessionFactory实例  
	        SessionFactory sessionFactory=configuration.buildSessionFactory();  
	        //获取一个全新的session对象  
	        Session session=sessionFactory.openSession();  
	        //创建一个事务  
	        org.hibernate.Transaction transaction = null;
	        try {  
	            //用session开启事务进行数据插入  
	            transaction=session.beginTransaction();  
	            session.save(guest);  
	            //提交事务  
	            transaction.commit();  
	        } catch (Exception e) {  
	            //如果数据插入失败这回滚到初始化状态  
	            if(transaction!=null){  
	                transaction.rollback();  
	                e.printStackTrace();  
	                return false;//报异常则插入失败
	            }  
	        }finally{  
	            //记得最后关闭session  
	            session.close();  
	        } 
			return true;  
	}


	@Override
	public Guest searchGuestById(String GuestId) {
		Configuration config = new Configuration().configure();
		ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
		sf = new Configuration().configure().buildSessionFactory(sr);
		String hql = " from Guest where guestPhone = :guestPhone";
		Session session = sf.openSession();
		Query query = session.createQuery(hql).setString("guestPhone", GuestId);
		Guest g = null;
		g = (Guest) query.uniqueResult();
		session.close();
		return g;
	}


	@Override
	public boolean updateGuest(Guest guest) {
		Configuration configuration = new Configuration().configure();
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(guest);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		session.close();
		return true;
	}



}
