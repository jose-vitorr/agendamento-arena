-- =====================================================================
-- Sistema de Agendamento de Arenas — Schema Inicial (MVP)
-- Flyway migration: V1__criacao_schema_inicial.sql
-- Banco: PostgreSQL
-- =====================================================================

-- ---------------------------------------------------------------------
-- EXTENSÕES
-- ---------------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =====================================================================
-- TABELA: arena
-- =====================================================================
CREATE TABLE arena (
                       id                                  BIGSERIAL PRIMARY KEY,
                       nome                                VARCHAR(150)    NOT NULL,
                       endereco                            VARCHAR(255)    NOT NULL,
                       link_maps                           VARCHAR(500),
                       telefone_whatsapp                   VARCHAR(20)     NOT NULL,
                       prazo_minimo_cancelamento_horas     INTEGER         NOT NULL DEFAULT 24,
                       percentual_reembolso_cancelamento   NUMERIC(5,2)    NOT NULL DEFAULT 100.00,
                       criado_em                           TIMESTAMP       NOT NULL DEFAULT now(),
                       atualizado_em                       TIMESTAMP       NOT NULL DEFAULT now(),

                       CONSTRAINT ck_arena_percentual_reembolso
                           CHECK (percentual_reembolso_cancelamento BETWEEN 0 AND 100)
);

COMMENT ON TABLE arena IS 'Arena esportiva cadastrada no sistema (multi-tenant)';

-- =====================================================================
-- TABELA: modalidade
-- =====================================================================
CREATE TABLE modalidade (
                            id      BIGSERIAL PRIMARY KEY,
                            nome    VARCHAR(50) NOT NULL,

                            CONSTRAINT uq_modalidade_nome UNIQUE (nome)
);

COMMENT ON TABLE modalidade IS 'Modalidades suportadas (Vôlei, Society, etc.)';

INSERT INTO modalidade (nome) VALUES ('Vôlei'), ('Society');

-- =====================================================================
-- TABELA: quadra
-- =====================================================================
CREATE TABLE quadra (
                        id          BIGSERIAL PRIMARY KEY,
                        arena_id    BIGINT NOT NULL REFERENCES arena(id) ON DELETE CASCADE,
                        nome        VARCHAR(100) NOT NULL,
                        criado_em   TIMESTAMP NOT NULL DEFAULT now(),

                        CONSTRAINT uq_quadra_arena_nome UNIQUE (arena_id, nome)
);

COMMENT ON TABLE quadra IS 'Quadra física pertencente a uma arena';

CREATE INDEX idx_quadra_arena ON quadra(arena_id);

-- =====================================================================
-- TABELA: quadra_modalidade (associativa N:M)
-- =====================================================================
CREATE TABLE quadra_modalidade (
                                   id              BIGSERIAL PRIMARY KEY,
                                   quadra_id       BIGINT NOT NULL REFERENCES quadra(id) ON DELETE CASCADE,
                                   modalidade_id   BIGINT NOT NULL REFERENCES modalidade(id) ON DELETE RESTRICT,
                                   valor_hora      NUMERIC(10,2) NOT NULL,

                                   CONSTRAINT uq_quadra_modalidade UNIQUE (quadra_id, modalidade_id),
                                   CONSTRAINT ck_quadra_modalidade_valor CHECK (valor_hora >= 0)
);

COMMENT ON TABLE quadra_modalidade IS 'Preço por hora de cada combinação quadra + modalidade';

CREATE INDEX idx_quadra_modalidade_quadra ON quadra_modalidade(quadra_id);
CREATE INDEX idx_quadra_modalidade_modalidade ON quadra_modalidade(modalidade_id);

-- =====================================================================
-- TABELA: usuario
-- =====================================================================
CREATE TABLE usuario (
                         id                  BIGSERIAL PRIMARY KEY,
                         nome                VARCHAR(150) NOT NULL,
                         telefone_whatsapp   VARCHAR(20)  NOT NULL,
                         email               VARCHAR(150) NOT NULL,
                         senha_hash          VARCHAR(255) NOT NULL,
                         criado_em           TIMESTAMP    NOT NULL DEFAULT now(),

                         CONSTRAINT uq_usuario_email UNIQUE (email)
);

COMMENT ON TABLE usuario IS 'Cliente que realiza reservas';

CREATE INDEX idx_usuario_telefone ON usuario(telefone_whatsapp);

-- =====================================================================
-- TABELA: administrador
-- =====================================================================
CREATE TABLE administrador (
                               id           BIGSERIAL PRIMARY KEY,
                               nome         VARCHAR(150) NOT NULL,
                               email        VARCHAR(150) NOT NULL,
                               senha_hash   VARCHAR(255) NOT NULL,
                               criado_em    TIMESTAMP    NOT NULL DEFAULT now(),

                               CONSTRAINT uq_administrador_email UNIQUE (email)
);

COMMENT ON TABLE administrador IS 'Responsável(is) por administrar arena(s)';

-- =====================================================================
-- TABELA: administrador_arena (associativa N:M)
-- =====================================================================
CREATE TABLE administrador_arena (
                                     id                  BIGSERIAL PRIMARY KEY,
                                     administrador_id    BIGINT NOT NULL REFERENCES administrador(id) ON DELETE CASCADE,
                                     arena_id            BIGINT NOT NULL REFERENCES arena(id) ON DELETE CASCADE,
                                     papel               VARCHAR(20) NOT NULL DEFAULT 'operador',

                                     CONSTRAINT uq_administrador_arena UNIQUE (administrador_id, arena_id),
                                     CONSTRAINT ck_administrador_arena_papel CHECK (papel IN ('dono', 'operador'))
);

COMMENT ON TABLE administrador_arena IS 'Vínculo N:M entre administradores e arenas que gerenciam';

CREATE INDEX idx_administrador_arena_admin ON administrador_arena(administrador_id);
CREATE INDEX idx_administrador_arena_arena ON administrador_arena(arena_id);

-- =====================================================================
-- TABELA: recorrencia
-- (estrutura pronta para o MVP; lógica de geração automática é feature futura)
-- =====================================================================
CREATE TABLE recorrencia (
                             id                  BIGSERIAL PRIMARY KEY,
                             usuario_id          BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
                             dia_semana          SMALLINT NOT NULL,
                             horario_referencia  TIME NOT NULL,
                             ativa               BOOLEAN NOT NULL DEFAULT true,
                             criado_em           TIMESTAMP NOT NULL DEFAULT now(),

                             CONSTRAINT ck_recorrencia_dia_semana CHECK (dia_semana BETWEEN 0 AND 6)
);

COMMENT ON TABLE recorrencia IS 'Série de reserva recorrente semanal (RN09) — geração automática é feature futura';

CREATE INDEX idx_recorrencia_usuario ON recorrencia(usuario_id);

-- =====================================================================
-- TABELA: horario
-- =====================================================================
CREATE TABLE horario (
                         id             BIGSERIAL PRIMARY KEY,
                         quadra_id      BIGINT NOT NULL REFERENCES quadra(id) ON DELETE CASCADE,
                         data           DATE NOT NULL,
                         hora_inicio    TIME NOT NULL,
                         hora_fim       TIME NOT NULL,
                         status         VARCHAR(25) NOT NULL DEFAULT 'disponivel',
                         bloqueado_ate  TIMESTAMP,
                         criado_em      TIMESTAMP NOT NULL DEFAULT now(),

                         CONSTRAINT uq_horario_quadra_data_hora UNIQUE (quadra_id, data, hora_inicio),
                         CONSTRAINT ck_horario_intervalo_valido CHECK (hora_fim > hora_inicio),
                         CONSTRAINT ck_horario_status CHECK (status IN ('disponivel', 'bloqueado_temporario', 'reservado'))
);

COMMENT ON TABLE horario IS 'Slot de disponibilidade de uma quadra em uma data/hora específica';

CREATE INDEX idx_horario_quadra_data ON horario(quadra_id, data);
CREATE INDEX idx_horario_status ON horario(status);
CREATE INDEX idx_horario_bloqueado_ate ON horario(bloqueado_ate)
    WHERE status = 'bloqueado_temporario';

-- =====================================================================
-- TABELA: reserva
-- =====================================================================
CREATE TABLE reserva (
                         id                          BIGSERIAL PRIMARY KEY,
                         usuario_id                  BIGINT NOT NULL REFERENCES usuario(id) ON DELETE RESTRICT,
                         horario_id                  BIGINT NOT NULL REFERENCES horario(id) ON DELETE RESTRICT,
                         modalidade_id               BIGINT NOT NULL REFERENCES modalidade(id) ON DELETE RESTRICT,
                         valor_total                 NUMERIC(10,2) NOT NULL,
                         valor_pago                  NUMERIC(10,2) NOT NULL DEFAULT 0,
                         status                      VARCHAR(20) NOT NULL DEFAULT 'pendente',

    -- Campos preparados para features futuras (nullable — sem impacto no MVP)
                         recorrencia_id               BIGINT REFERENCES recorrencia(id) ON DELETE SET NULL,
                         saldo_confirmado_em          TIMESTAMP,
                         confirmado_por_admin_id      BIGINT REFERENCES administrador(id) ON DELETE SET NULL,

                         criado_em                    TIMESTAMP NOT NULL DEFAULT now(),
                         atualizado_em                TIMESTAMP NOT NULL DEFAULT now(),

                         CONSTRAINT uq_reserva_horario UNIQUE (horario_id),
                         CONSTRAINT ck_reserva_valor_total CHECK (valor_total >= 0),
                         CONSTRAINT ck_reserva_valor_pago CHECK (valor_pago >= 0),
                         CONSTRAINT ck_reserva_status CHECK (status IN ('pendente', 'confirmada', 'cancelada'))
);

COMMENT ON TABLE reserva IS 'Reserva de um horário por um usuário';
COMMENT ON COLUMN reserva.horario_id IS 'UNIQUE garante que um horário só pode ter uma reserva ativa por vez';
COMMENT ON COLUMN reserva.recorrencia_id IS 'Preenchido apenas quando a reserva pertence a uma série recorrente (RN09 — feature futura)';
COMMENT ON COLUMN reserva.saldo_confirmado_em IS 'Preenchido quando o admin confirma recebimento do saldo presencial (UC14)';

CREATE INDEX idx_reserva_usuario ON reserva(usuario_id);
CREATE INDEX idx_reserva_status ON reserva(status);
CREATE INDEX idx_reserva_recorrencia ON reserva(recorrencia_id);

-- =====================================================================
-- TABELA: pagamento
-- =====================================================================
CREATE TABLE pagamento (
                           id                      BIGSERIAL PRIMARY KEY,
                           reserva_id              BIGINT NOT NULL REFERENCES reserva(id) ON DELETE CASCADE,
                           valor                   NUMERIC(10,2) NOT NULL,
                           forma_pagamento         VARCHAR(20) NOT NULL,
                           status                  VARCHAR(20) NOT NULL DEFAULT 'pendente',
                           valor_reembolsado       NUMERIC(10,2),
                           gateway_transacao_id    VARCHAR(100),
                           criado_em               TIMESTAMP NOT NULL DEFAULT now(),
                           atualizado_em           TIMESTAMP NOT NULL DEFAULT now(),

                           CONSTRAINT ck_pagamento_valor CHECK (valor >= 0),
                           CONSTRAINT ck_pagamento_valor_reembolsado CHECK (valor_reembolsado >= 0),
                           CONSTRAINT ck_pagamento_forma CHECK (forma_pagamento IN ('pix', 'cartao')),
                           CONSTRAINT ck_pagamento_status CHECK (status IN ('pendente', 'aprovado', 'recusado', 'reembolsado'))
);

COMMENT ON TABLE pagamento IS 'Tentativas/transações de pagamento associadas a uma reserva';

CREATE INDEX idx_pagamento_reserva ON pagamento(reserva_id);
CREATE INDEX idx_pagamento_status ON pagamento(status);
CREATE UNIQUE INDEX uq_pagamento_gateway_transacao ON pagamento(gateway_transacao_id)
    WHERE gateway_transacao_id IS NOT NULL;

-- =====================================================================
-- TABELA: notificacao
-- =====================================================================
CREATE TABLE notificacao (
                             id             BIGSERIAL PRIMARY KEY,
                             reserva_id     BIGINT NOT NULL REFERENCES reserva(id) ON DELETE CASCADE,
                             canal          VARCHAR(20) NOT NULL,
                             status_envio   VARCHAR(20) NOT NULL DEFAULT 'pendente',
                             data_envio     TIMESTAMP,

                             CONSTRAINT ck_notificacao_canal CHECK (canal IN ('whatsapp', 'site')),
                             CONSTRAINT ck_notificacao_status CHECK (status_envio IN ('pendente', 'enviado', 'falhou'))
);

COMMENT ON TABLE notificacao IS 'Registro de notificações de confirmação enviadas ao usuário';

CREATE INDEX idx_notificacao_reserva ON notificacao(reserva_id);

-- =====================================================================
-- FUNCTIONS E TRIGGERS — atualização automática de "atualizado_em"
-- =====================================================================
CREATE OR REPLACE FUNCTION fn_atualiza_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.atualizado_em = now();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_arena_atualizado_em
    BEFORE UPDATE ON arena
    FOR EACH ROW EXECUTE FUNCTION fn_atualiza_timestamp();

CREATE TRIGGER trg_reserva_atualizado_em
    BEFORE UPDATE ON reserva
    FOR EACH ROW EXECUTE FUNCTION fn_atualiza_timestamp();

CREATE TRIGGER trg_pagamento_atualizado_em
    BEFORE UPDATE ON pagamento
    FOR EACH ROW EXECUTE FUNCTION fn_atualiza_timestamp();

-- =====================================================================
-- FIM DO SCHEMA INICIAL (MVP)
-- =====================================================================