/**
 * 营销平台落地页 SDK
 * 用法：
 *   <script src="http://127.0.0.1:8000/sdk/landing.js"
 *           data-url="https://你的落地页地址"
 *           data-api="http://127.0.0.1:8000"></script>
 *
 * 功能：
 *   1. 按当前页面地址拉取绑定的微信号（/cvslink/flg）
 *   2. 右下角渲染微信号卡片（二维码 + 复制/长按按钮）
 *   3. 复制、长按等行为自动上报（/monitor/append），带 eventId 幂等
 */
(function () {
  var script = document.currentScript;
  if (!script) return;

  var api = (script.getAttribute('data-api') || location.origin).replace(/\/$/, '');
  var pageUrl = script.getAttribute('data-url') || location.href.split('#')[0];
  var state = { landingId: null, wxList: [] };

  function uuid() {
    if (window.crypto && crypto.randomUUID) return crypto.randomUUID();
    return 'evt-' + Date.now() + '-' + Math.random().toString(36).slice(2, 10);
  }

  function post(path, body) {
    return fetch(api + path, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body || {})
    }).then(function (r) { return r.json().catch(function () { return null; }); });
  }

  function pad(n) { return n < 10 ? '0' + n : '' + n; }
  function today() {
    var d = new Date();
    return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate());
  }
  function now() {
    var d = new Date();
    return pad(d.getHours()) + ':' + pad(d.getMinutes()) + ':' + pad(d.getSeconds());
  }

  function report(wx, type) {
    if (!state.landingId) return;
    post('/monitor/append', {
      eventId: uuid(),
      url: state.landingId,
      wx: wx.wxh,
      cvsdate: today(),
      cvstime: now(),
      stay: 0,
      type: type,
      targetelement: 'sdk',
      city: {},
      equipment: navigator.platform || '',
      rate: '',
      search: '',
      total: 1,
      report: 0
    });
  }

  function copy(text) {
    if (navigator.clipboard && navigator.clipboard.writeText) {
      return navigator.clipboard.writeText(text).catch(function () {});
    }
    var input = document.createElement('textarea');
    input.value = text;
    document.body.appendChild(input);
    input.select();
    try { document.execCommand('copy'); } catch (e) {}
    document.body.removeChild(input);
    return Promise.resolve();
  }

  function style(el, css) {
    for (var k in css) { if (Object.prototype.hasOwnProperty.call(css, k)) el.style[k] = css[k]; }
  }

  function render(link) {
    var group = link.landing && link.landing.Wxgroup;
    var list = (group && group.Weixin) || [];
    if (!list.length) return;
    state.wxList = list;

    var panel = document.createElement('div');
    panel.id = '__landing_sdk__';
    style(panel, {
      position: 'fixed', right: '16px', bottom: '16px', zIndex: 2147483000,
      width: '260px', background: '#fff', borderRadius: '14px',
      boxShadow: '0 8px 30px rgba(0,0,0,.18)', fontFamily: '-apple-system,BlinkMacSystemFont,Segoe UI,Microsoft YaHei,sans-serif',
      padding: '14px', boxSizing: 'border-box', color: '#1f2329'
    });

    var close = document.createElement('div');
    close.textContent = '×';
    style(close, { position: 'absolute', right: '10px', top: '6px', cursor: 'pointer', color: '#999', fontSize: '18px' });
    close.onclick = function () { panel.remove(); };
    panel.appendChild(close);

    var wx = list[0];
    var title = document.createElement('div');
    title.textContent = wx.wxname || '添加客服微信';
    style(title, { fontWeight: '600', fontSize: '14px', marginBottom: '8px', paddingRight: '16px' });
    panel.appendChild(title);

    var qr = document.createElement('img');
    qr.alt = '二维码';
    style(qr, { width: '100%', borderRadius: '10px', background: '#f5f6f8', display: 'block' });
    if (wx.erweima) {
      qr.src = wx.erweima;
    } else {
      qr.style.display = 'none';
    }
    panel.appendChild(qr);

    var wxh = document.createElement('div');
    wxh.textContent = '微信号：' + (wx.wxh || '');
    style(wxh, { fontSize: '12px', color: '#646a73', margin: '10px 0' });
    panel.appendChild(wxh);

    var copyBtn = document.createElement('button');
    copyBtn.textContent = '复制微信号';
    style(copyBtn, {
      width: '100%', padding: '9px 0', border: '0', borderRadius: '9px',
      background: '#2f6bff', color: '#fff', fontSize: '13px', cursor: 'pointer', marginBottom: '6px'
    });
    copyBtn.onclick = function () {
      copy(wx.wxh).then(function () {
        copyBtn.textContent = '已复制，去微信添加';
        setTimeout(function () { copyBtn.textContent = '复制微信号'; }, 1800);
      });
      report(wx, 3);
    };
    panel.appendChild(copyBtn);

    var pressBtn = document.createElement('button');
    pressBtn.textContent = '长按识别二维码';
    style(pressBtn, {
      width: '100%', padding: '9px 0', border: '1px solid #dcdfe6', borderRadius: '9px',
      background: '#fff', color: '#1f2329', fontSize: '13px', cursor: 'pointer'
    });
    pressBtn.onclick = function () { report(wx, 2); };
    panel.appendChild(pressBtn);

    document.body.appendChild(panel);
  }

  post('/cvslink/flg', { words: pageUrl }).then(function (link) {
    if (!link || !link.landing) {
      console.warn('[landing-sdk] 未找到落地页配置：' + pageUrl);
      return;
    }
    state.landingId = link.landing.id;

    var key = '__landing_view__' + state.landingId;
    try {
      if (!sessionStorage.getItem(key)) {
        sessionStorage.setItem(key, '1');
        var group = link.landing.Wxgroup;
        var first = group && group.Weixin && group.Weixin[0];
        post('/monitor/inctotal', { url: state.landingId, wx: first ? first.wxh : '' });
      }
    } catch (e) {}

    render(link);
  });
})();
