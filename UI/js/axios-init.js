let clientId = 'xxxxx';
let redirectUri = 'http://office.aulang.cn/index.html';
let baseUrl = 'http://office.aulang.cn';

// 配置请求baseURL
axios.defaults.baseURL = baseUrl;
// 允许跨域
axios.defaults.crossDomain = true;
// 允许跨域携带Cookie
axios.defaults.withCredentials = true;
// 设置请求头为 Authorization
axios.defaults.headers.common['Authorization'] = '';


// 设置Authorization请求头Token
axios.interceptors.request.use(
    config => {
        let token = window.sessionStorage.getItem("token");
        token && (config.headers.Authorization = 'Bearer ' + token);
        return config;
    },
    error => {
        return Promise.error(error);
    }
);


axios.interceptors.response.use(
    response => {
        // 如果返回的状态码为200，说明接口请求成功，可以正常拿到数据，否则的话抛出错误
        if (response.status === 200) {
            return Promise.resolve(response);
        } else {
            return Promise.reject(response);
        }
    },
    // 服务器状态码不是2开头的的情况
    error => {
        if (error.response.status) {
            switch (error.response.status) {
                // 401: 未登录，跳转登录页面
                case 401:
                    redirectLoginUrl();
                    break;
                // 403 登录过期，跳转登录页面
                case 403:
                    // 清除token
                    window.sessionStorage.removeItem("token");
                    redirectLoginUrl();
                    break;
                default:
                    return Promise.reject(error.response);
            }
            return Promise.reject(error.response);
        }
    }
);

function redirectLoginUrl() {
    let scope = 'basic';
    let responseType = 'code';
    let encodeRedirectUri = encodeURI(redirectUri);

    let state = Math.random().toString(36).slice(0, 6);
    window.sessionStorage.setItem('oauth_state', state);

    let url = `https://aulang.cn/oauth/authorize?client_id=${clientId}&response_type=${responseType}&redirect_uri=${encodeRedirectUri}&scope=${scope}&state=${state}`;
    window.location.replace(url);
}

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

function loginHandle() {
    let code = urlParam('code');
    let state = urlParam('state');

    if (!code || !state) {
        return;
    }

    let currState = window.sessionStorage.getItem('oauth_state');
    if (state !== currState) {
        console.warn('不合法的state');
        return;
    }

    window.sessionStorage.removeItem('oauth_state');

    let postData = {
        code: code,
        redirectUrl: redirectUri
    }

    axios.post('/oauth/token', postData)
        .then(response => {
            let result = response.data;
            let token = result.access_token;
            window.sessionStorage.setItem('token', token);
        })
        .catch(error => {
            alert(error.data || '登录失败！');
        });
}