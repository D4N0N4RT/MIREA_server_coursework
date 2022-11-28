package ru.mirea.server_coursework.repository;

import com.github.tennaito.rsql.jpa.JpaPredicateVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public abstract class AbstractRepository<M, I> {

    @PersistenceContext
    protected EntityManager entityManager;
    protected CriteriaBuilder criteriaBuilder;
    protected Class<M> clazz;


    @PostConstruct
    public void initAbstract() {
        criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public abstract void init();

    public void setClazz(final Class<M> clazzToSet) {
        clazz = Preconditions.checkNotNull(clazzToSet, null);
    }

    public M findOne(@NonNull Specification<M> specification) {
        CriteriaQuery<M> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<M> root = criteriaQuery.from(clazz);

        criteriaQuery
                .select(root);

        Predicate predicate = specification.toPredicate(root, criteriaQuery, criteriaBuilder);
        criteriaQuery
                .where(predicate);

        try {
            return entityManager.createQuery(criteriaQuery).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public M findById(@NonNull I id) {
        return entityManager.find(clazz, id);
    }

    private <T> List<M> findAll(@NonNull CriteriaQuery<M> criteriaQuery,
                                @NonNull From<T, M> from,
                                @Nullable List<Predicate> predicateList,
                                @Nullable List<Order> orderList
    ) {
        criteriaQuery
                .select(from);

        if (predicateList != null) {
            criteriaQuery
                    .where(predicateList.toArray(new Predicate[0]));
        }

        if (orderList != null) {
            criteriaQuery
                    .orderBy(orderList);
        }

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public List<M> findAllByPredicateAndOrderList(
            @Nullable List<Predicate> predicateList, @Nullable List<Order> orderList
    ) {
        CriteriaQuery<M> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<M> root = criteriaQuery.from(clazz);

        return findAll(criteriaQuery, root, predicateList, orderList);
    }

    public List<M> findAll(@Nullable Specification<M> specification, @Nullable Sort sort) throws WrongRSQLQueryException {
        return findAllByQuery(specification, null, sort);
    }

    public List<M> findAll() throws WrongRSQLQueryException {
        return findAll(null, null);
    }

    public List<M> findAll(Specification<M> specification) throws WrongRSQLQueryException {
        return findAll(specification, null);
    }

    public List<M> findAll(Sort sort) throws WrongRSQLQueryException {
        return findAll(null, sort);
    }

    public List<M> findAllByQuery(@Nullable String rsqlQuery, @Nullable Sort sort) throws WrongRSQLQueryException {
        return findAllByQuery(null, rsqlQuery, sort);
    }

    public List<M> findAllByQuery(@Nullable Specification<M> specification,
                                  @Nullable String rsqlQuery,
                                  @Nullable Sort sort
    ) throws WrongRSQLQueryException {
        CriteriaQuery<M> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<M> root = criteriaQuery.from(clazz);

        List<Predicate> predicateList = new LinkedList<>();
        if (rsqlQuery != null) {
            RSQLVisitor<Predicate, EntityManager> visitor = new JpaPredicateVisitor<M>().defineRoot(root);

            Node rootNode;
            try {
                rootNode = new RSQLParser().parse(rsqlQuery);
                predicateList.add(rootNode.accept(visitor, entityManager));
            } catch (Exception e) {
                String message = "Не уалось прочитать строку RSQL запроса";

                throw new WrongRSQLQueryException(message);
            }
        }

        if (specification != null) {
            predicateList.add(specification.toPredicate(root, criteriaQuery, criteriaBuilder));
        }

        List<Order> orderList = null;
        if (sort != null) {
            orderList = QueryUtils.toOrders(sort, root, criteriaBuilder);
        }

        return findAll(criteriaQuery, root, predicateList, orderList);
    }

    public void delete(M model) {
        entityManager.remove(model);
    }

    public void create(M newModel) {
        entityManager.persist(newModel);
    }

    public void update(M changedModel) {
        entityManager.merge(changedModel);
    }

    public boolean isExist(M model) {
        return entityManager.contains(model);
    }

    public boolean isExist(Specification<M> specification) throws WrongRSQLQueryException {
        return !findAll(specification).isEmpty();
    }

    public Integer size() throws WrongRSQLQueryException {
        return findAll().size();
    }
}
