let baseUrl = 'http://office.aulang.cn/api/';
let callbackUrl = baseUrl + 'office/callback';

let goBackUrl = 'http://office.aulang.cn/index.html';


var config = {
    documentType: 'text',
    width: '100%',
    height: '100%'
};

var editorConfig = {
    lang: 'zh-CN',
    region: 'zh-CN',
    callbackUrl: callbackUrl,
    customization: {
        goback: {
            blank: true,
            text: '返回文档列表',
            url: goBackUrl
        }
    }
};

function urlParam(name) {
    let search = window.location.search;
    if (!search) {
        return null;
    }

    let query = search.substring(1);
    let params = query.split('&');

    for (let i = 0; i < params.length; i++) {
        let pair = params[i].split('=');
        if (pair[0] === name) {
            return pair[1];
        }
    }

    return null;
}

var id = urlParam('id');
if (id) {
    axios.get(baseUrl + 'doc/' + id)
        .then(response => {
            let result = response.data;
            let fileUrl = baseUrl + 'office/doc/' + id;

            config.documentType = result.documentType;

            editorConfig.user = {
                id: result.owner,
                name: result.ownerName
            };

            let documentConfig = {
                fileType: result.fileType,
                key: result.key,
                title: result.name,
                url: fileUrl,
                info: {
                    folder: result.owner,
                    owner: result.ownerName,
                    uploaded: result.createTime
                }
            };

            config.document = documentConfig;
            config.editorConfig = editorConfig;

            openWithToken(config);
        })
        .catch(error => {
            alert(error.data || '加载文件失败');
        });
} else {
    alert('缺少文档ID！');
}

function openWithToken(config) {
    let postData = {
        'content': JSON.stringify(config)
    };

    axios.post(baseUrl + 'office/token', postData)
        .then(response => {
            config.token = response.data;
            new DocsAPI.DocEditor('placeholder', config);
        })
        .catch(error => {
            alert(error.data || '获取令牌失败');
        });
}