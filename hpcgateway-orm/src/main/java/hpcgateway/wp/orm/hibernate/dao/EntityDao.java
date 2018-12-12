package hpcgateway.wp.orm.hibernate.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.dao.support.DaoSupport;
import org.springframework.orm.hibernate5.HibernateTemplate;

public abstract class EntityDao extends DaoSupport
{
	protected HibernateTemplate hibernateTemplate;

	public SessionFactory getSessionFactory() {
		return (this.hibernateTemplate != null ? this.hibernateTemplate.getSessionFactory() : null);
	}
	
	protected HibernateTemplate createHibernateTemplate(SessionFactory sessionFactory) {
		return new HibernateTemplate(sessionFactory);
	}

	
	public <T> T fetch(Class<T> clazz,String hql,Map<String,Object> map)
	{
		Session session = getSessionFactory().getCurrentSession();
		Query<T> query = session.createQuery(hql, clazz);
		for(String name : map.keySet())
		{
			query.setParameter(name, map.get(name));
		}
		List<T> list = query.getResultList();
		if( list == null || list.isEmpty() )
		{
			return null;
		}
		return list.get(0);
	}
	
	public <T> T fetch(Class<T> clazz,String hql,String name,Object value)
	{
		Session session = getSessionFactory().getCurrentSession();
		Query<T> query = session.createQuery(hql, clazz);
		query.setParameter(name, value);
		List<T> list = query.getResultList();
		if( list == null || list.isEmpty() )
		{
			return null;
		}
		return list.get(0);
	}
	
	public int count(String hql,String name,Object value)
	{
		Session session = getSessionFactory().getCurrentSession();
		Query<Long> query = session.createQuery(hql, Long.class);
		query.setParameter(name, value);
		Long cnt = query.getSingleResult();
		return cnt.intValue();
	}
	
	public int count(String hql,Map<String,Object> map)
	{
		Session session = getSessionFactory().getCurrentSession();
		Query<Long> query = session.createQuery(hql, Long.class);
		for(String key : map.keySet())
		{
			query.setParameter(key, map.get(key));
		}
		Long cnt = query.getSingleResult();
		return cnt.intValue();
	}
	
	public <T> List<T> query(Class<T> clazz,String hql) throws Exception
	{
		Session session = getSessionFactory().getCurrentSession();
		Query<T> query = session.createQuery(hql, clazz);
		return query.getResultList();
	}
	
	public <T> List<T> query(Class<T> clazz,String hql,String name,Object value,Integer pageNo,Integer pageSize)
	{
		Session session = getSessionFactory().getCurrentSession();
		Query<T> query = session.createQuery(hql, clazz);
		query.setParameter(name, value);
		if( pageNo != null && pageNo > 0 && pageSize != null && pageSize > 0 )
		{
			int first = (pageNo-1)*pageSize;
			query.setFirstResult(first);
			query.setFetchSize(pageSize);
			query.setMaxResults(pageSize);
		}
		return query.getResultList();
	}
	
	public <T> List<T> query(Class<T> clazz,String hql,Map<String,Object> map,Integer pageNo,Integer pageSize)
	{
		Session session = getSessionFactory().getCurrentSession();
		Query<T> query = session.createQuery(hql, clazz);
		if( map != null && !map.isEmpty() )
		{
			for(String key : map.keySet())
			{
				query.setParameter(key, map.get(key));
			}
		}
		if( pageNo != null && pageNo > 0 && pageSize != null && pageSize > 0 )
		{
			int first = (pageNo-1)*pageSize;
			query.setFirstResult(first);
			query.setFetchSize(pageSize);
			query.setMaxResults(pageSize);
		}
		return query.getResultList();
	}
	
	public <T> void save(Class<T> clazz,T object)
	{
		Session session = getSessionFactory().getCurrentSession();
		session.saveOrUpdate(clazz.getName(), object);
	}
	
	public <T> void delete(T object)
	{
		Session session = getSessionFactory().getCurrentSession();
		session.remove(object);
	}
	
	public <T> void delete(Class<T> clazz,long id)
	{
		String hql = String.format("delete %s where id=:id",clazz.getName());
		T object = fetch(clazz,hql,"id",id);
		delete(object);
	}
	
	public int execute(String hql,Map<String,Object> map)
	{
		Session session = getSessionFactory().getCurrentSession();
		Query<?> query = session.createQuery(hql);
		if( map != null && !map.isEmpty() )
		{
			for(String key : map.keySet())
			{
				query.setParameter(key, map.get(key));
			}
		}
		return query.executeUpdate();
	}
	
	public int execute(String hql,String name,Object value)
	{
		Session session = getSessionFactory().getCurrentSession();
		Query<?> query = session.createQuery(hql);
		query.setParameter(name,value);
		return query.executeUpdate();
	}

	public int execute(String hql)
	{
		return execute(hql,null);
	}
	
}
