package br.com.postech.sevenfoodpay.application.database.repository;

import br.com.postech.sevenfoodpay.application.database.mapper.PaymentMapper;
import br.com.postech.sevenfoodpay.core.domain.PaymentDomain;
import br.com.postech.sevenfoodpay.core.ports.out.PaymentRepositoryPort;
import br.com.postech.sevenfoodpay.infraestruture.entities.PaymentEntity;
import br.com.postech.sevenfoodpay.infraestruture.repository.PaymentRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentRepositoryAdapter(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public PaymentDomain save(PaymentDomain paymentDomain) {
        PaymentEntity paymentEntity = paymentMapper.toEntity(paymentDomain);
        PaymentEntity save = paymentRepository.save(paymentEntity);
        return paymentMapper.toDomain(save);
    }

    @Override
    public boolean remove(Long id) {
        try {
            paymentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public PaymentDomain findById(Long id) {
        Optional<PaymentEntity> buClient = paymentRepository.findById(id);
        if (buClient.isPresent()) {
            return paymentMapper.toDomain(buClient.get());
        }
        return null;
    }

    @Override
    public List<PaymentDomain> findAll() {
        List<PaymentEntity> allPayments = paymentRepository.findAll();
        return paymentMapper.map(allPayments);
    }

    @Override
    public PaymentDomain update(Long id, PaymentDomain paymentDomain) {
        Optional<PaymentEntity> resultById = paymentRepository.findById(id);
        if (resultById.isPresent()) {

            PaymentEntity clientToChange = resultById.get();
            clientToChange.update(id, paymentDomain);

            return paymentMapper.toDomain(paymentRepository.save(clientToChange));
        }

        return null;
    }
}
