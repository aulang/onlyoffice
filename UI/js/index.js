loginHandle(() => getDocs(1, 20));

let fileTypes = ['.doc', '.docx', '.odt', '.txt', '.xls', '.xlsx', '.ods', '.csv', '.ppt', '.pptx', '.odt'];

let index = new Vue({
    el: '#index',
    data: {
        page: 1,
        pageSize: 20,
        totalPages: 1,
        content: []
    },
    computed: {
        noPrevious: function () {
            return (this.page === 1);
        },
        noNext: function () {
            return (this.totalPages === 0 || this.page === this.totalPages);
        }
    },
    methods: {
        open: function (e) {
            let filename = e.target.value;

            if (!filename) {
                return;
            }

            let fileType = filename.substr(filename.indexOf('.'));
            if (fileTypes.indexOf(fileType) === -1) {
                alert('文件格式不对');
                return;
            }

            let config = {
                headers: {
                    'Content-Type': 'multipart/form-data;boundary = ' + new Date().getTime()
                }
            }
            let formData = new FormData();
            formData.append('file', e.target.files[0]);

            axios.post('/api/doc', formData, config)
                .then(response => {
                    let result = response.data;

                    window.open('./editor.html?id=' + result.id, '_blank').focus();
                })
                .catch(error => {
                    alert(error.data || '打开文档失败');
                });
        },
        edit: function (id) {
            window.open('./editor.html?id=' + id, '_blank').focus();
        },
        download: function (id) {
            axios.get('/api/doc/' + id)
                .then(response => {
                    let result = response.data;

                    if ('saved' === result.status) {
                        downloadFile(baseUrl + '/api/doc/' + id + '/download');
                    } else {
                        alert('文档正在编辑或保存中，请稍后再进行下载！');
                    }
                })
                .catch(error => {
                    alert(error.data || '获取文档信息失败');
                });
        },
        del: function (id, name) {
            let flag = confirm('确认删除【' + name + '】');
            if (flag) {
                axios.delete('/api/doc/' + id)
                    .then(() => {
                        alert('删除成功！');
                        getDocs(index.page, index.pageSize);
                    })
                    .catch(error => {
                        alert(error.data || '删除失败');
                    });
            }
        },
        goPage: function (page) {
            getDocs(page, this.pageSize);
        }
    }
});

function downloadFile(url) {
    let token = window.sessionStorage.getItem("token");
    let a = document.createElement('a');
    a.href = url + '?access_token=' + token;
    a.style.display = 'none';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
}

function getDocs(page, pageSize) {
    page = page || 1;
    pageSize = pageSize || 20;

    axios.get('/api/doc/list', {page: page, pageSize: pageSize})
        .then(response => {
            let result = response.data;

            index.page = result.page;
            index.pageSize = result.pageSize;
            index.totalPages = result.totalPages;
            index.content = result.content;
        })
        .catch(error => {
            alert(error.data || '加载文件列表失败');
        });
}

let freshFlag = false;
window.onfocus = function () {
    if (freshFlag) {
        getDocs(index.page, index.pageSize);
        freshFlag = false;
    }
};

window.onblur = function () {
    freshFlag = true;
};