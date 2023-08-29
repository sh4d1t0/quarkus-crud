package org.orquestador.credit.repositories;

import java.util.List;
import java.util.function.Supplier;

import org.orquestador.credit.entities.Credit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CreditRepository implements PanacheRepositoryBase<Credit, Long> {
    private static final Logger log = LoggerFactory.getLogger(CreditRepository.class);

    Supplier<Uni<List<Credit>>> sortedCreditSupplier = () -> this.listAllSorted("clientName");

    public Uni<List<Credit>> listAllSorted(String sortCriteria) {
        return Panache.withTransaction(() -> Credit.listAll(Sort.by(sortCriteria)));
    }

    public Uni<List<Credit>> getAll() {
        log.info("Fetching all credit by clientName");
        return sortedCreditSupplier.get();
    }

    public Uni<Credit> getById(Long id) {
        log.info("Fetching credit by ID: {}", id);
        return Panache.withTransaction(() -> Credit.findById(id));
    }

    public Uni<Credit> create(Credit credit) {
        log.info("Creating credit: {}", credit.getClientName());
        return Panache.withTransaction(credit::persist)
                .replaceWith(credit);
    }

    public Uni<Credit> updateProperties(Credit existingCredit, Credit credit) {
        // INFO Client Data
        existingCredit.setClientName(credit.getClientName());
        existingCredit.setClientLastName(credit.getClientLastName());
        existingCredit.setClientMaternalLastName(credit.getClientMaternalLastName());
        existingCredit.setRelationInvex(credit.getRelationInvex());
        existingCredit.setBusinessName(credit.getBusinessName());
        existingCredit.setRfc(credit.getRfc());
        return existingCredit.persistAndFlush().replaceWith(existingCredit);
    }

    public Uni<Credit> update(Long id, Credit credit) {
        log.info("updating credit with ID: {}", id);
        return Panache.withTransaction(() -> Credit.<Credit>findById(id)
                .onItem().ifNotNull().transformToUni(existingCredit -> updateProperties(existingCredit, credit)));
    }

    public Uni<Credit> persistOrUpdate(Credit credit) {
        return Panache.withTransaction(credit::persist).replaceWith(credit);
    }

    public Uni<Credit> delete(Long id) {
        return Panache.withTransaction(() -> Credit.<Credit>findById(id)
                .onItem().ifNotNull().transformToUni(credit -> credit.delete().replaceWith(credit)));
    }
}
