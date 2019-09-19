/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uesocc.edu.sv.ingenieria.prn335.guia04.boundary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import uesocc.edu.sv.ingenieria.prn335.guia04.control.ClasificacionFacade;
import uesocc.edu.sv.ingenieria.prn335.guia04.control.exceptions.NonexistentEntityException;

/**
 *
 * @author elmer
 */
@ManagedBean
@RequestScoped
@Named(value = "ClasificacionBean")
public class ClasificacionBean implements Serializable {

    private static List<ClasificacionFacade> listClasificacion = new ArrayList();

    private ClasificacionFacade clasi = new ClasificacionFacade();

    public ClasificacionBean() {
    }

    public ClasificacionFacade getClasi() {
        return clasi;
    }

    public void setClasi(ClasificacionFacade clasi) {
        this.clasi = clasi;
    }

    public List<ClasificacionFacade> getListPersona() {
        return listClasificacion;
    }

    public void setListClasificacion(List<ClasificacionFacade> listClasificacion) {
        this.listClasificacion = listClasificacion;
    }

    public ClasificacionBean(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    
        public void agregarClasificacion(){
    this.listClasificacion.add(this.clasi);
    }
    
    
    
    public void eliminarClasificacion(ClasificacionFacade cl){
        ClasificacionBean.listClasificacion.remove(cl);
    }
    
    public void crear(ClasificacionBean clasificacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(clasificacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void modificar(ClasificacionBean clasificacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            clasificacion = em.merge(clasificacion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                //Integer id = clasificacion.getIdClasificacion();
//                if (findClasificacion(id) == null) {
//                   // throw new NonexistentEntityException("The clasificacion with id " + id + " no longer exists.");
//                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void eliminar(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClasificacionBean clasificacion;
            try {
                clasificacion = em.getReference(ClasificacionBean.class, id);
                //clasificacion.getIdClasificacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clasificacion with id " + id + " no longer exists.", enfe);
            }
            em.remove(clasificacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ClasificacionBean> findClasificacionEntities() {
        return findClasificacionEntities(true, -1, -1);
    }

    public List<ClasificacionBean> findClasificacionEntities(int maxResults, int firstResult) {
        return findClasificacionEntities(false, maxResults, firstResult);
    }

    private List<ClasificacionBean> findClasificacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClasificacionBean.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ClasificacionBean findClasificacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClasificacionBean.class, id);
        } finally {
            em.close();
        }
    }

    public int getClasificacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ClasificacionBean> rt = cq.from(ClasificacionBean.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
