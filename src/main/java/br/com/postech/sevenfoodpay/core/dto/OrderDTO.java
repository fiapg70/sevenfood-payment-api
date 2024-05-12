package br.com.postech.sevenfoodpay.core.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class OrderDTO implements Serializable {
    private Long id;
    private String code;
    private List<ProductDTO> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return Objects.equals(id, orderDTO.id) && Objects.equals(code, orderDTO.code) && Objects.equals(products, orderDTO.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, products);
    }
}
