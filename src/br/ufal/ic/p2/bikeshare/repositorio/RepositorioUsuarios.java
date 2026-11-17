package br.ufal.ic.p2.bikeshare.repositorio;

import br.ufal.ic.p2.bikeshare.excecoes.EntidadeDuplicadaException;
import br.ufal.ic.p2.bikeshare.excecoes.EntidadeNaoExisteException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.modelo.usuario.Usuario;

public final class RepositorioUsuarios extends RepositorioComSequencia<Usuario> {
    private static final long serialVersionUID = 1L;

    public RepositorioUsuarios() {
        super("U", Mensagens.ID_USUARIO_NULO, Mensagens.USUARIO_NAO_EXISTE);
    }

    @Override
    public void adicionar(Usuario usuario) {
        exigirEmailLivre(usuario.getEmail(), usuario);
        super.adicionar(usuario);
    }

    /** O email é único ignorando maiúsculas; o próprio dono pode "repeti-lo". */
    public void exigirEmailLivre(String email, Usuario dono) {
        for (Usuario u : valores()) {
            if (u != dono && u.getEmail().equalsIgnoreCase(email)) {
                throw new EntidadeDuplicadaException(Mensagens.EMAIL_JA_CADASTRADO);
            }
        }
    }

    public Usuario buscarPorEmail(String email) {
        for (Usuario u : valores()) {
            if (u.getEmail().equalsIgnoreCase(email.trim())) {
                return u;
            }
        }
        throw new EntidadeNaoExisteException(Mensagens.USUARIO_NAO_EXISTE);
    }
}
