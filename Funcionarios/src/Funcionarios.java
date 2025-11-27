import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

class Agente {
    private int id;
    private String nome;
    private int idade;
    private String classe;

    public Agente() {}

    public Agente(int id, String nome, int idade, String classe) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.classe = classe;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }

    public String getClasse() { return classe; }
    public void setClasse(String classe) { this.classe = classe; }

    @Override
    public String toString() {
        return "ID: " + id + " | Nome: " + nome + " | Idade: " + idade + " | Classe: " + classe;
    }

    public Object[] toArray() {
        return new Object[]{id, nome, classe, idade};
    }
}

class GerenciadorAgentes {
    private List<Agente> agentes;
    private int proximoId;

    public GerenciadorAgentes() {
        this.agentes = new ArrayList<>();
        this.proximoId = 1;
    }

    public void adicionar(Agente agente) {
        agente.setId(proximoId++);
        agentes.add(agente);
    }

    public List<Agente> obterTodos() {
        return new ArrayList<>(agentes);
    }

    public Agente buscarPorId(int id) {
        return agentes.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean atualizar(int id, Agente agenteAtualizado) {
        for (int i = 0; i < agentes.size(); i++) {
            Agente agente = agentes.get(i);
            if (agente.getId() == id) {
                agenteAtualizado.setId(id);
                agentes.set(i, agenteAtualizado);
                return true;
            }
        }
        return false;
    }

    public boolean remover(int id) {
        return agentes.removeIf(a -> a.getId() == id);
    }

    public int total() {
        return agentes.size();
    }
}

public class Funcionarios extends JFrame {
    private GerenciadorAgentes gerenciador = new GerenciadorAgentes();
    private DefaultTableModel modelo;
    private JTable tabela;
    private JTextField campoNome, campoIdade, campoClasse, campoBuscar;
    private JButton botaoAdicionar, botaoEditar, botaoExcluir, botaoBuscar, botaoLimpar;

    private final Color COR_PRINCIPAL = new Color(129, 19, 229);
    private final Color COR_SECUNDARIA = new Color(72, 0, 98);
    private final Color COR_TERCIARIA = new Color(104, 63, 147);
    private final Color COR_TEXTO_ESCURO = new Color(49, 2, 2);
    private final Color COR_TEXTO_CLARO = Color.BLACK;
    private final Color COR_BOTOES = Color.WHITE;

    public Funcionarios() {
        super("Infinity - Gerenciador de Funcionarios");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        getContentPane().setBackground(COR_PRINCIPAL);

        criarComponentes();
        montarInterface();
        configurarAcoes();

        carregarDados();
    }

    private void criarComponentes() {
        modelo = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Função", "Idade"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component componente = super.prepareRenderer(renderer, row, column);

                componente.setBackground(COR_TERCIARIA);
                componente.setForeground(COR_TEXTO_CLARO);

                if (isRowSelected(row)) {
                    componente.setBackground(new Color(50, 100, 150));
                    componente.setForeground(Color.WHITE);
                }

                return componente;
            }
        };

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setBackground(COR_SECUNDARIA);
        tabela.setForeground(COR_TEXTO_CLARO);
        tabela.setGridColor(new Color(100, 100, 100));
        tabela.setSelectionBackground(new Color(50, 100, 150));
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setFont(new Font("Arial", Font.PLAIN, 12));
        tabela.setRowHeight(25);

        campoNome = criarCampo();
        campoIdade = criarCampo();
        campoClasse = criarCampo();
        campoBuscar = criarCampo();

        botaoAdicionar = criarBotao("Novo Usuario");
        botaoEditar = criarBotao("Alterar");
        botaoExcluir = criarBotao("Remover");
        botaoBuscar = criarBotao("Procurar");
        botaoLimpar = criarBotao("Limpar");
    }

    private JTextField criarCampo() {
        JTextField campo = new JTextField(20);
        campo.setBackground(Color.WHITE);
        campo.setForeground(COR_TEXTO_ESCURO);
        campo.setCaretColor(COR_TEXTO_ESCURO);
        campo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        campo.setFont(new Font("Arial", Font.PLAIN, 12));
        return campo;
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setBackground(COR_BOTOES);
        botao.setForeground(COR_TEXTO_ESCURO);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setFont(new Font("Arial", Font.BOLD, 12));

        botao.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                botao.setBackground(new Color(240, 240, 240));
            }
            public void mouseExited(MouseEvent evt) {
                botao.setBackground(COR_BOTOES);
            }
        });

        return botao;
    }

    private JLabel criarRotulo(String texto) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setForeground(COR_TEXTO_ESCURO);
        rotulo.setFont(new Font("Arial", Font.BOLD, 12));
        return rotulo;
    }

    private void montarInterface() {
        setLayout(new BorderLayout(10, 10));

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COR_TEXTO_ESCURO), "Dados do Funcionario"));
        painelFormulario.setBackground(COR_PRINCIPAL);
        ((javax.swing.border.TitledBorder)painelFormulario.getBorder())
                .setTitleColor(COR_TEXTO_ESCURO);
        ((javax.swing.border.TitledBorder)painelFormulario.getBorder())
                .setTitleFont(new Font("Arial", Font.BOLD, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(criarRotulo("Nome:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painelFormulario.add(criarRotulo("Função:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(campoClasse, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painelFormulario.add(criarRotulo("Idade:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(campoIdade, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setBackground(COR_PRINCIPAL);
        painelBotoes.add(botaoAdicionar);
        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoExcluir);
        painelFormulario.add(painelBotoes, gbc);

        JPanel painelPesquisa = new JPanel(new FlowLayout());
        painelPesquisa.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COR_TEXTO_ESCURO), "Localizar Funcionario"));
        painelPesquisa.setBackground(COR_PRINCIPAL);
        ((javax.swing.border.TitledBorder)painelPesquisa.getBorder())
                .setTitleColor(COR_TEXTO_ESCURO);
        ((javax.swing.border.TitledBorder)painelPesquisa.getBorder())
                .setTitleFont(new Font("Arial", Font.BOLD, 12));
        painelPesquisa.add(criarRotulo("Nome:"));
        painelPesquisa.add(campoBuscar);
        painelPesquisa.add(botaoBuscar);
        painelPesquisa.add(botaoLimpar);

        JScrollPane painelRolagem = new JScrollPane(tabela);
        painelRolagem.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COR_TEXTO_ESCURO), "Usuario Cadastrados"));
        painelRolagem.setBackground(COR_PRINCIPAL);
        ((javax.swing.border.TitledBorder)painelRolagem.getBorder())
                .setTitleColor(COR_TEXTO_ESCURO);
        ((javax.swing.border.TitledBorder)painelRolagem.getBorder())
                .setTitleFont(new Font("Arial", Font.BOLD, 12));

        tabela.getTableHeader().setBackground(COR_TERCIARIA);
        tabela.getTableHeader().setForeground(COR_TEXTO_CLARO);
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        painelRolagem.getViewport().setBackground(COR_SECUNDARIA);

        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.setBackground(COR_PRINCIPAL);
        painelSuperior.add(painelFormulario, BorderLayout.NORTH);
        painelSuperior.add(painelPesquisa, BorderLayout.SOUTH);

        add(painelSuperior, BorderLayout.NORTH);
        add(painelRolagem, BorderLayout.CENTER);
    }

    private void configurarAcoes() {
        botaoAdicionar.addActionListener(e -> incluirAgente());

        botaoEditar.addActionListener(e -> modificarAgente());

        botaoExcluir.addActionListener(e -> excluirAgente());

        botaoBuscar.addActionListener(e -> pesquisarAgentes());

        botaoLimpar.addActionListener(e -> {
            campoBuscar.setText("");
            carregarDados();
        });

        tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    preencherFormulario();
                }
            }
        });

        ActionListener acaoEnter = e -> incluirAgente();
        campoNome.addActionListener(acaoEnter);
        campoIdade.addActionListener(acaoEnter);
        campoClasse.addActionListener(acaoEnter);
    }

    private void incluirAgente() {
        try {
            String nome = campoNome.getText().trim();
            String idadeStr = campoIdade.getText().trim();
            String classe = campoClasse.getText().trim();

            if (nome.isEmpty() || idadeStr.isEmpty() || classe.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idade = Integer.parseInt(idadeStr);

            Agente agente = new Agente(0, nome, idade, classe);
            gerenciador.adicionar(agente);

            limparCampos();
            carregarDados();
            JOptionPane.showMessageDialog(this, "Funcionario registrado com sucesso!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A idade deve ser um valor numérico!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarAgente() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionario para modificar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) modelo.getValueAt(linhaSelecionada, 0);
            String nome = campoNome.getText().trim();
            String idadeStr = campoIdade.getText().trim();
            String classe = campoClasse.getText().trim();

            if (nome.isEmpty() || idadeStr.isEmpty() || classe.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idade = Integer.parseInt(idadeStr);

            Agente agenteModificado = new Agente(id, nome, idade, classe);

            if (gerenciador.atualizar(id, agenteModificado)) {
                limparCampos();
                carregarDados();
                JOptionPane.showMessageDialog(this, "Dados atualizados!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A idade deve ser um valor numérico!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirAgente() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionario para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modelo.getValueAt(linhaSelecionada, 0);
        String nome = (String) modelo.getValueAt(linhaSelecionada, 1);

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Confirmar exclusão  " + nome + "?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            if (gerenciador.remover(id)) {
                limparCampos();
                carregarDados();
                JOptionPane.showMessageDialog(this, "Funcionario removido do sistema!");
            }
        }
    }

    private void pesquisarAgentes() {
        String termo = campoBuscar.getText().trim();
        if (termo.isEmpty()) {
            carregarDados();
            return;
        }

        modelo.setRowCount(0);
        for (Agente agente : gerenciador.obterTodos()) {
            if (agente.getNome().toLowerCase().contains(termo.toLowerCase())) {
                modelo.addRow(agente.toArray());
            }
        }
    }

    private void preencherFormulario() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            campoNome.setText(modelo.getValueAt(linhaSelecionada, 1).toString());
            campoClasse.setText(modelo.getValueAt(linhaSelecionada, 2).toString());
            campoIdade.setText(modelo.getValueAt(linhaSelecionada, 3).toString());
        }
    }

    private void carregarDados() {
        modelo.setRowCount(0);
        for (Agente agente : gerenciador.obterTodos()) {
            modelo.addRow(agente.toArray());
        }
    }

    private void limparCampos() {
        campoNome.setText("");
        campoIdade.setText("");
        campoClasse.setText("");
        tabela.clearSelection();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new Funcionarios().setVisible(true);
        });
    }
}