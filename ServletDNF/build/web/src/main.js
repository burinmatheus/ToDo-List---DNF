document.querySelector('#form').addEventListener('submit', function(e) {
    e.preventDefault();

    let todo = document.getElementById('todo').value;
    let funcao = 'adicionar';
    let caminho = 'EJBdonotforget/Funcoes';

    document.getElementById('todo').value = '';

    let corpo = `conteudo=${todo}&funcao=${funcao}&caminho=${caminho}`;
    requisicao('/donotforget/ServletDNF', corpo, alertresposta, alerterror);
});

function requisicao(url, corpo, callbackOk, callbackErro) {
    var http = new XMLHttpRequest();
    http.open("POST", url, true);
    http.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    http.addEventListener('load', function() {
        if (http.status < 400)
            callbackOk(http.response);
        else
            callbackErro(http.response);

    });
    http.addEventListener('error', function() {
        callbackErro(`Erro de Rede!`);
    });
    http.send(corpo);
}

function alertresposta(resp) {
    console.log(JSON.parse(resp).data);
    lista();
}

function alerterror(resp) {
    alert(`Error: ${JSON.parse(resp).erro}`);
    lista();
}

function processalista(resp) {
    let respostas = JSON.parse(resp).todos;

    let box = document.getElementById("todoList");
    box.innerHTML = ' ';

    respostas.forEach(resposta => {
        let todobox = document.createElement('div');
        todobox.classList.add('todobox');
        todobox.setAttribute('id', resposta.id);
        todobox.setAttribute('ondblclick', 'mudastatus(this)');

        if (resposta.status == '1') {
            todobox.classList.add('concluida');
        }

        let icone = document.createElement('span');
        icone.classList.add('icone');
        icone.innerHTML = "&#10004;";

        let p = document.createElement('p');
        p.classList.add('conteudo');
        p.innerHTML = resposta.conteudo;

        let remover = document.createElement('i');
        remover.classList.add('fas');
        remover.classList.add('fa-trash-alt');
        remover.setAttribute('onclick', 'remove(this)');


        todobox.appendChild(icone);
        todobox.appendChild(p);
        todobox.appendChild(remover);
        box.appendChild(todobox);

    });

}

function lista() {
    let funcao = 'listar';
    let caminho = 'EJBdonotforget/Funcoes';

    let corpo = `funcao=${funcao}&caminho=${caminho}`;
    requisicao('/donotforget/ServletDNF', corpo, processalista, alerterror);

}

function remove(element) {
    element.style.fontSize = '20em';
    element.style.right = '0';
    element.style.backgroundColor = 'red';

    corpo = 'id=' + element.offsetParent.id + '&funcao=deletar&caminho=EJBdonotforget/Funcoes';

    setTimeout(function() {
        requisicao('/donotforget/ServletDNF', corpo, alertresposta, alerterror);
    }, 500);

}

function mudastatus(element) {
    let corpo = '';
    if (element.classList.contains("concluida")) {
        if (confirm('Você realmente deseja marcar esta atividade como não concluída?')) {
            corpo = 'id=' + element.id + '&status=0&funcao=editar&caminho=EJBdonotforget/Funcoes';
        }
    } else {
        corpo = 'id=' + element.id + '&status=1&funcao=editar&caminho=EJBdonotforget/Funcoes';
    }
    requisicao('/donotforget/ServletDNF', corpo, alertresposta, alerterror);
}

lista();
alert('Clique 2x sobre uma tarefa para marcá-la como concluída.');