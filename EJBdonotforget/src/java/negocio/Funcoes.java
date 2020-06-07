package negocio;

import dao.DAODNF;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelDNF;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Matheus M. Burin
 */
@Stateless
public class Funcoes {

    public String adicionar(HttpServletRequest request, HttpServletResponse response) {

        int status = 0;
        String conteudo = "";

        LocalDateTime agora = LocalDateTime.now();

        DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String data = formatterData.format(agora);

        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        String hora = formatterHora.format(agora);

        DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date datasql = null;

        DateFormat fmt1 = new SimpleDateFormat("HH:mm:ss");
        java.sql.Time horasql = null;

        try {
            datasql = new java.sql.Date(fmt.parse(data).getTime());
            horasql = new java.sql.Time(fmt1.parse(hora).getTime());
        } catch (ParseException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (request.getParameter("conteudo") != null) {
            conteudo = request.getParameter("conteudo");
        }

        ModelDNF dnf = new ModelDNF();
        dnf.setStatus(status);
        dnf.setConteudo(conteudo);
        dnf.setData(datasql);
        dnf.setHora(horasql);

        try {
            new DAODNF().adiconar(dnf);
            response.setStatus(200);
            return ("{\"data\":\"Salvo com sucesso!\"}");
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao salvar!\"}");
        }

    }

    public String editar(HttpServletRequest request, HttpServletResponse response) {

        int id = 0, status = 0;

        if (request.getParameter("id") != null) {
            id = Integer.parseInt(request.getParameter("id"));
        }

        if (request.getParameter("status") != null) {
            status = Integer.parseInt(request.getParameter("status"));
        }

        ModelDNF dnf = new ModelDNF();
        dnf.setId(id);
        dnf.setStatus(status);

        try {
            new DAODNF().editar(dnf);
            response.setStatus(200);
            return ("{\"data\":\"Editado com sucesso!\"}");
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao editar!\"}");
        }

    }

    public String listar(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        List<ModelDNF> todos = new DAODNF().buscarTodos();

        JSONObject retorno = new JSONObject();

        JSONArray arrayRetorno = new JSONArray();
        JSONObject json;
        for (ModelDNF todo : todos) {

            json = new JSONObject();

            json.put("id", todo.getId());
            json.put("status", todo.getStatus());
            json.put("conteudo", todo.getConteudo());
            json.put("data", todo.getData());
            json.put("hora", todo.getHora());

            arrayRetorno.put(json);
        }

        retorno.put("todos", arrayRetorno);

        return retorno.toString();

    }

    public String deletar(HttpServletRequest request, HttpServletResponse response) {

        int id = Integer.parseInt(request.getParameter("id"));

        try {
            new DAODNF().deletar(id);
            response.setStatus(200);
            return ("{\"data\":\"Excluído com sucesso!\"}");
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao excluir!\"}");
        }
    }

}
