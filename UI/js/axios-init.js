// 允许跨域
axios.defaults.crossDomain = true;
// 允许跨域携带Cookie
axios.defaults.withCredentials = true;
// 配置请求baseURL
//axios.defaults.baseURL = 'http://office.aulang.cn';
// 设置请求头为 Authorization
axios.defaults.headers.common['Authorization'] = '';


// 设置Authorization请求头Token
axios.interceptors.request.use(
    config => {
        const token = window.sessionStorage.getItem("token");
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

let clientId = 'xxxxx';
let redirectUri = encodeURI('http://office.aulang.cn/index.html');

function redirectLoginUrl() {
    let scope = 'basic';
    let responseType = 'code';
    let encodeRedirectUri = encodeURI(redirectUri);

    let state = Math.random().toString(36).slice(0, 6);
    window.sessionStorage.setItem('oauth_state', state);

    let url = `https://aulang.cn/oauth/authorize?client_id=${clientId}&response_type=${responseType}&redirect_uri=${encodeRedirectUri}&scope=${scope}&state=${state}`;
    window.location.replace(url);
}