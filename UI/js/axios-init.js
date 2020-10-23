let baseUrl = 'http://office.aulang.cn';
let clientId = '5f37d9f4c4155cda795f8fe5';
let redirectUri = encodeURI('http://office.aulang.cn/index.html');

// 配置请求baseURL
axios.defaults.baseURL = baseUrl;
// 允许跨域
axios.defaults.crossDomain = true;
// 允许跨域携带Cookie
axios.defaults.withCredentials = false;
// 设置请求头为 Authorization
axios.defaults.headers.common['Authorization'] = '';


// 设置Authorization请求头Token
axios.interceptors.request.use(
    config => {
        let token = window.localStorage.getItem("token");
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
                    // 清除token
                    window.localStorage.removeItem("token");
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

    let state = random(6);
    window.localStorage.setItem('oauth_state', state);

    let url = `https://aulang.cn/oauth/authorize?client_id=${clientId}&response_type=${responseType}&redirect_uri=${redirectUri}&scope=${scope}&state=${state}`;
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

function random(length) {
    let str = Math.random().toString(36).substr(2);
    if (str.length >= length) {
        return str.substr(0, length);
    }
    str += random(length - str.length);
    return str;
}

function loginHandle(loginFn) {
    let token = window.localStorage.getItem("token");
    if (token) {
        // 已登录
        loginFn();
        return;
    }

    let code = urlParam('code');
    let state = urlParam('state');

    if (!code || !state) {
        // 未登录没有code和state
        redirectLoginUrl();
        return;
    }

    let currState = window.localStorage.getItem('oauth_state');
    if (state !== currState) {
        // 不合法的state
        alert('不合法的state！');
        redirectLoginUrl();
        return;
    }

    window.localStorage.removeItem('oauth_state');

    let params = {
        code: code,
        redirectUrl: redirectUri
    }

    axios.get('/api/oauth/token', {params: params})
        .then(response => {
            // 登录成功
            let result = response.data;
            let token = result.access_token;
            window.localStorage.setItem('token', token);

            loginFn();
        })
        .catch(error => {
            // 登录失败
            alert(error.data || '登录失败！');
            redirectLoginUrl();
        });
}