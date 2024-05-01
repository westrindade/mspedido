INSERT INTO tb_pedidos (
    cd_pedido, cd_cliente, dt_criacao, nu_valor_total, ds_forma_pagamento,
    nu_quantidade_parcelas, ds_status_pedido, dt_pagamento, dt_entrega,
    dt_cancelamento, cd_cep, ds_logradouro, ds_numero, ds_complemento,
    nm_bairro, nm_cidade, nm_estado
) VALUES (
    10, 1, CURRENT_TIMESTAMP, 100.00, 1, 10, 3, CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP + INTERVAL '1' DAY, NULL, '06550-000', 'Rua 28',
    '123', 'Apto 101', 'Centro', 'São Paulo', 'SP'
);