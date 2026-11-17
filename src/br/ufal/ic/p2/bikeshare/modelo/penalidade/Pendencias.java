package br.ufal.ic.p2.bikeshare.modelo.penalidade;

import br.ufal.ic.p2.bikeshare.util.Dinheiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Penalidades de um usuário. Havendo valor pendente, ele é considerado inadimplente. */
public final class Pendencias implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ArrayList<Penalidade> penalidades = new ArrayList<>();

    public void adicionar(Penalidade penalidade) {
        penalidades.add(penalidade);
    }

    public BigDecimal getTotal() {
        BigDecimal total = Dinheiro.ZERO;
        for (Penalidade p : penalidades) {
            total = total.add(p.getValor());
        }
        return Dinheiro.arredondar(total);
    }

    public boolean existem() {
        return getTotal().signum() > 0;
    }

    public List<Penalidade> listar() {
        return Collections.unmodifiableList(new ArrayList<>(penalidades));
    }
}
