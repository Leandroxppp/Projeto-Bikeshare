package br.ufal.ic.p2.bikeshare.modelo.bicicleta;

import br.ufal.ic.p2.bikeshare.config.Parametros;
import br.ufal.ic.p2.bikeshare.excecoes.AtributoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.modelo.Identificavel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Bicicleta compartilhada. O ciclo de vida é delegado ao {@link EstadoBicicleta}; o que muda
 * entre convencional e elétrica (bateria e adicional de tarifa) é polimórfico.
 */
public abstract class Bicicleta implements Identificavel, Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private EstadoBicicleta estado = EstadoBicicleta.DISPONIVEL;

    protected Bicicleta(String id) {
        this.id = id;
    }

    @Override
    public final String getId() {
        return id;
    }

    public final EstadoBicicleta getEstado() {
        return estado;
    }

    public abstract String getTipo();

    /** Convencional: sempre suficiente. Elétrica: bateria no mínimo configurado. */
    public abstract boolean temBateriaSuficiente(Parametros parametros);

    /** Valor extra por minuto cobrado sobre a tarifa do plano. */
    public abstract BigDecimal getAdicionalPorMinuto(Parametros parametros);

    protected abstract String consultarAtributoEspecifico(String atributo);

    // ---------------------------------------------------------------- transições

    /** Recusa a retirada se a bicicleta não estiver no estado disponível (ex.: em manutenção). */
    public final void exigirDisponivel() {
        estado.aoRetirar();
    }

    public final void retirar() {
        estado = estado.aoRetirar();
    }

    public final void devolver() {
        estado = estado.aoDevolver();
    }

    public final void devolverComExpiracao() {
        estado = estado.aoDevolverComExpiracao();
    }

    public final void lancarDefeito() {
        estado = estado.aoLancarDefeito();
    }

    public final void concluirManutencao() {
        estado = estado.aoConcluirManutencao();
    }

    public final void exigirRemovivel() {
        estado.exigirRemovivel();
    }

    /** Disponível no estado e com bateria suficiente: é o que conta nos relatórios de disponibilidade. */
    public final boolean estaDisponivelParaRetirada(Parametros parametros) {
        return estado == EstadoBicicleta.DISPONIVEL && temBateriaSuficiente(parametros);
    }

    /** Atributos próprios da bicicleta; a estação é resolvida pelo serviço, que conhece o mapa. */
    public final String consultar(String atributo) {
        if ("tipo".equals(atributo)) {
            return getTipo();
        }
        if ("estado".equals(atributo)) {
            return estado.descricao();
        }
        return consultarAtributoEspecifico(atributo);
    }

    protected static AtributoException atributoInexistente() {
        return new AtributoException(Mensagens.ATRIBUTO_NAO_EXISTE);
    }
}
