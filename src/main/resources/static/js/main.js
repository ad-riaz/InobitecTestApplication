var table;

/*
* The initial function when loading the page
*
* */
$(document).ready(function () {
    // Указание на таблицу с комментариями
    table = $("#comments-tree");
    // Основная функция библиотеки
    table.treetable({ expandable: true });
    // Вызов функции добавления комментариев верхнего уровня
    insertComments(getFirstLevelCommentsRequest(), null, table);

    // Обработка отправки формы
    $(document).on('submit', '#addRootCommentForm', function() {
        element = $('#addRootCommentForm');
        addNewComment(element);
        document.getElementById("comment-field").value = "";
        return false;
    });
});

/*
* Download child nodes for comment and expand/collapse it.
*
* @param element – the element on which the 'expand/collapse' button was pressed
*
* */
function expandAndCollapseComments(element) {
    // Массив всех комментов в таблице
    let allCommentsFromTable = document.getElementsByClassName('comment');
    // Подгружены ли уже дочерние комментарии в DOM для данного комментария
    let thereIsNoChildComments = true;
    // Текущий комментарий, на котором произошло нажатие
    let currentComment = jQuery(element);
    // Родительский комментарий
    let parentOfComment = currentComment.closest('.comment');
    // Путь родительского комментария
    let parentOfCommentPath = parentOfComment.attr('path');
    // ID родительского комментария
    let parentOfCommentId = parentOfComment.attr('data-tt-id');
    // Путь, по которому будут искаться комментарии в базе
    let childrenCommentsPathForGetRequest = parentOfCommentPath + '.*{1}';
    // Регулярное выражение для проверки наличия дочерних комментариев
    let childrenCommentsPathForRegex = "^(" + parentOfCommentPath + "\.[1-9])";

    $(".comment-actions").click(function(event){
        event.stopPropagation();
    });

    // Если комментарий скрыт/сложен
    if (parentOfComment.hasClass("comment-collapsed")) {
        // Получаем массив комментов
        Array.prototype.forEach.call(allCommentsFromTable, function (item) {
            // Путь каждого комментария в массиве
            let itemPath = $(item).attr('path');
            // Если путь не проходит проверку по регулярному выражению (то есть нет ни одного дочернего комментария для текущего), то возвращается null
            let regexp = itemPath.match(childrenCommentsPathForRegex);
            // Проверяем, есть ли дочерние комментарии
            if (regexp !== null) {
                thereIsNoChildComments = false;
            }
        });

        // Если нет дочерних комментариев, то делаем запрос на их получение
        if (thereIsNoChildComments) {
            // Получение список дочерних комментариев
            childrenComments = getCommentsByPathRequest(childrenCommentsPathForGetRequest);
            // Если список не пустой
            if (childrenComments.length !== 0) {
                // Вставить элементы в DOM внутрь родительского комментария
                insertComments(childrenComments, parentOfCommentId, table);
            }
        }
        // Раскрыть комментарий
        expandComment(parentOfComment);
    } else if (parentOfComment.hasClass("comment-expanded")) {
        // Свернуть комментарий
        collapseComment(parentOfComment);
    }
}

/*
* Add and remove classes in comment to expand it.
*
* @param element – comment
* @param parent – comment's parent element
*
* */
function expandComment(element) {
    // Удалить класс comment-collapsed и добавить класс comment-expanded для комментария
    element.removeClass("comment-collapsed").addClass("comment-expanded");
}

/*
* Add and remove classes in comment to collapse it.
*
* @param element – comment
* @param parent – comment's parent element
*
* */
function collapseComment(element) {
    // Удалить класс comment-expanded и добавить класс comment-collapsed для комментария
    element.removeClass("comment-expanded").addClass("comment-collapsed");
}

/*
* Insert comment into HTML code
*
* @param arrayOfComments – the array containing comments
* @param parentCommentId – the id of element to which the comment will be added
* @param commentContainer – comment table
*
* */
function insertComments(arrayOfComments, parentCommentId, commentContainer) {
    let parentNode = null;

    // Если ID родительского комментария не равен NULL
    if (parentCommentId !== null) {
        // Получаем treetableNode родительского комментария
        parentNode = commentContainer.treetable("node", parentCommentId);
    }

    // Для каждого комментария в массиве
    Array.prototype.forEach.call(arrayOfComments, function (item) {
        // Создаем HTML код для него и заполняем поля
        comment = returnCommentHTML(item.id, parentCommentId, item.path, item.comment);
        // Вставляем в DOM текущий комментарий
        commentContainer.treetable("loadBranch", parentNode, comment);
    });
}

/*
* Returns HTML code of comment
*
* @param commentId
* @param parentCommentId
* @param commentPath
* @param commentBody
*
* @returns string with a comment
* */
function returnCommentHTML(commentId, parentCommentId, commentPath, commentBody) {
    // Общая часть строки для комментария
    let commonHTML  =   "<td class=\"comment-td\" onclick=\"expandAndCollapseComments(this)\">" +
                            commentBody +
                        "</td>"+
                        "<td>" +
                            "<form class=\"comment-actions addCommentForm\" onsubmit=\"addNewComment(this);return false\">" +
                                "<textarea rows=\"3\" cols=\"25\" class=\"comment-actions_comment-field comment-textarea\" placeholder=\"Comment...\"></textarea>" +
                                "<input type=\"submit\" value=\"Reply\" class=\"replyOnComment addNewComment btn btn-primary\">" +
                                "<button type=\"button\" class=\"deleteComment btn btn-outline-danger\" onclick=\"deleteComments(this);\">" +
                                    "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-x-circle-fill\" viewBox=\"0 0 16 16\">\n" + "<path d=\"M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM5.354 4.646a.5.5 0 1 0-.708.708L7.293 8l-2.647 2.646a.5.5 0 0 0 .708.708L8 8.707l2.646 2.647a.5.5 0 0 0 .708-.708L8.707 8l2.647-2.646a.5.5 0 0 0-.708-.708L8 7.293 5.354 4.646z\"></path>\n</svg>" +
                                    "Delete" +
                                "</button>" +
                            "</form>" +
                        "</td>";


    if (parentCommentId === null || parentCommentId === undefined) {
        // Часть HTML кода для комментариев первого уровня
        return  "<tr data-tt-id=\"" + commentId + "\" path=\"" + commentPath + "\" class=\"comment comment-collapsed\">" + commonHTML + "</tr>";
    } else {
        // Часть HTML кода для вложенных комментариев
        return  "<tr data-tt-id=\"" + commentId + "\" data-tt-parent-id=\"" + parentCommentId + "\" path=\"" + commentPath + "\" class=\"comment comment-collapsed\">" + commonHTML + "</tr>";
    }
}



/*
* Adds a new comment to table and creates POST request to create new comment in database
*
* @param element – comment to be added
*
* */
function addNewComment(element) {
    // Форма добавления комментария
    let form = jQuery(element);
    // Элемент textarea
    let formTextArea = form.find(".comment-textarea");
    // Строка текста, записанная в textarea
    let commentBody = formTextArea.val();
    // Родительский комментарий, для которого добавляется новый комментарий (reply)
    let parentComment = formTextArea.closest(".comment");
    // Переменная, которая указывает на то, является ли эта форма формой ответа на другой комментарий или формой добавления нового комментария верхнего уровня
    let isThisReplyForm = true;
    // ID родительского комментария
    let parentCommentId;
    // Path родительского комментария
    let parentCommentPath;
    // treetableNode родительского комментария
    let parentTreetableNode;

    // Если нет родительского комментария для текущей формы
    if (parentComment[0] === null || parentComment[0] === undefined) {
        // То это не форма ответа
        isThisReplyForm = false;
    }

    // Если это все-таки форма ответа на комментарий
    if (isThisReplyForm) {
        // Получаем ID комментария, на который отвечаем
        parentCommentId = parentComment.attr("data-tt-id");
        // Получаем ID комментария, на который отвечаем
        parentCommentPath = parentComment.attr("path");
        // Получаем TreetableNode комментария, на который отвечаем
        parentTreetableNode = table.treetable("node", parentCommentId);
    } else {
        // Если это форма создания нового комментария верхнего уровня, то родительского комментария не существует. Все поля пустые.
        parentCommentId = null;
        parentCommentPath = '';
        parentTreetableNode = null;
    }

    try {
        // AJAX запрос на добавление нового комментария в базу
        addNewCommentRequsest(parentCommentPath, commentBody);
    } catch (e) {
        console.log(e.toString());
        alert(e.toString());
    }

    // Очистить textarea
    formTextArea.val("");

    // Если был добавлен комментарий верхнего уровня, то обновить страницу
    if (parentCommentId === null || parentCommentId === undefined) {
        window.location.reload();
    } else {
        let commentTd = parentComment.find(".comment-td");
        expandAndCollapseComments(commentTd);
    }

    return false;
}


/*
* Removes comment from the table and create DELETE request from the database
*
* @param element – comment to be deleted
*
* */
function deleteComments(element) {
    // Найти комментарий, внутри которого находится кнопка удаления
    let commentId = jQuery(element).closest(".comment").attr("data-tt-id");
    // Удалить из DOM комментарий по его ID
    table.treetable("removeNode", commentId);
    // Удалить комментарий из базы по его ID
    removeCommentRequest(commentId);
}

// AJAX requests

// Create requests
/*
* Add comment to server
*
* @param path – new comment path,
* @param body – new comment text
*
* @returns response
*
* */
function addNewCommentRequsest(path, body) {
    // Если path не определен, то это комментарий верхнего уровня
    if (path === null || path === undefined) {
        path = '';
    }

    // Поле комментария не может быть пустым
    if (body === null || body === undefined || body === '') {
        throw new Error("Comment can't be empty!");
    }

    // Тело запроса
    let requestBody = JSON.stringify({
        comment: body,
        path: path
    });

    let response;
    $.ajax({
        type: 'post',
        url: 'comments',
        headers: {
            "Content-Type": "application/json; odata=verbose"
        },
        data: requestBody,
        async: false,
        success: function (data) {
            response = data;
        },
        error: function (data) {
            alert("Error! Comment wasn't added.");
            console.log(data);
        }
    });

    return response;
}

// Get requests
/*
* Downloads first level comments from server
*
* @returns response
*
* */
function getFirstLevelCommentsRequest() {
    // Получение списка комментариев верхнего уровня
    return getCommentsByPathRequest("*{1}");
}


/*
* Downloads comments from the server using path
*
* @param path – path of the comments to be downloaded
*
* @returns response
*
* */
function getCommentsByPathRequest(path) {
    let response;
    $.ajax({
        type: 'get',
        url: 'comments/comment',
        dataType: "json",
        data: {
            path: path,
        },
        async: false,
        success: function (data) {
            response = data;
        },
        error: function (data) {
            console.log("Error! Comments wasn't downloaded by path: ");
            console.log(data);
        }
    });

    return response;
}


/*
* Downloads comments from the server by id
*
* @param id – id of the comment to be downloaded
*
* @returns response
*
* */
function getCommentById(id) {
    let response;
    let path = "comments/comment/" + id;

    $.ajax({
        type: 'get',
        url: path,
        dataType: "json",
        async: false,
        success: function (data) {
            response = data;
        },
        error: function (data) {
            console.log("Error! Comments wasn't downloaded by id:" + id);
            console.log(data);
        }
    });

    return response;
}


// Delete requests
/*
* Deletes comment from the database by id
*
* @param id – id of the comment to be deleted
*
* @returns response
*
* */
function removeCommentRequest(id) {
    let response;
    $.ajax({
        type: 'delete',
        url: 'comments/comment/' + id,
        dataType: "json",
        async: false,
        success: function (data) {
            response = data;
        },
        error: function (data) {
            console.log(data);
        }
    });

    return response;
}