package com.example.demo.dto;

import com.example.demo.entity.Produto;


import java.util.List;

public class PedidoDto {

    private String cliente;
    private String status;

    private List<Produto> produtos;

    public String getStatus() {
        return status;
    }

    public String getCliente() {
        return cliente;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }


}
