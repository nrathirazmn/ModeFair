(function(){
  // Helper to create HTML elements easily
  function h(tag, attrs, ...children){
    const el = document.createElement(tag);
    if (attrs) {
      Object.entries(attrs).forEach(([k, v]) => {
        if (k === 'class') el.className = v;
        else el.setAttribute(k, v);
      });
    }
    children.flat().forEach(c => {
      if (typeof c === 'string') el.appendChild(document.createTextNode(c));
      else if (c) el.appendChild(c);
    });
    return el;
  }

  // Get API key from multiple sources
  function resolveApiKey(opts){
    const params = new URLSearchParams(location.search);
    const urlKey = params.get('key');
    const metaKey = document.querySelector('meta[name="payflow-api-key"]')?.content;
    const attrKey = document.querySelector(opts.selector || '#payflow-widget')?.dataset?.key;
    const storeKey = (typeof localStorage !== 'undefined') ? localStorage.apiKey : null;
    return opts.apiKey || urlKey || attrKey || metaKey || storeKey || null;
  }

  // Render payment form
  function render(container, opts){
    const form = h('form', { class: 'pf-form' });

    form.append(
      h('div', {}, h('label', {}, 'Amount'), h('input', { id: 'pf-amount', readOnly: true, value: (opts.amount / 100).toFixed(2) })),
      h('div', {}, h('label', {}, 'Email'), h('input', { id: 'pf-email', type: 'email', required: true })),
      h('div', {}, h('label', {}, 'Name'), h('input', { id: 'pf-name', required: true })),
      h('div', {}, h('label', {}, 'Card Number'), h('input', { id: 'pf-card', placeholder: '4242 4242 4242 4242', required: true })),
      h('div', {}, h('label', {}, 'Expiry (MM/YY)'), h('input', { id: 'pf-exp', placeholder: '12/29', required: true })),
      h('div', {}, h('label', {}, 'CVV'), h('input', { id: 'pf-cvv', placeholder: '123', required: true })),
      h('div', {}, h('label', {}, 'Country'), h('input', { id: 'pf-cty' })),
      h('div', {}, h('label', {}, 'Postcode'), h('input', { id: 'pf-pc' })),
      h('button', { type: 'submit' }, 'Pay')
    );

    // Handle form submission
    form.addEventListener('submit', async (e) => {
      e.preventDefault();

      const body = {
        amount: opts.amount,
        currency: opts.currency || 'MYR',
        customerEmail: document.getElementById('pf-email').value,
        customerName: document.getElementById('pf-name').value,
        cardNumber: document.getElementById('pf-card').value.replace(/\s+/g, ''),
        expiry: document.getElementById('pf-exp').value,
        cvv: document.getElementById('pf-cvv').value,
        country: document.getElementById('pf-cty').value,
        postcode: document.getElementById('pf-pc').value
      };

      const apiKey = resolveApiKey(opts);
      if (!apiKey) {
        alert('PayFlow: API key not found. Add ?key=..., data-key, meta tag, or login to the portal first.');
        return;
      }

      try {
        const res = await fetch('http://localhost:8080/api/payments', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'X-PAYFLOW-KEY': apiKey
          },
          body: JSON.stringify(body)
        });

        const text = await res.text(); // read raw text
        let data = null;

        if (text && text.length > 0) {
          try {
            data = JSON.parse(text);
          } catch (parseErr) {
            throw new Error('Invalid JSON returned from server: ' + (text.length > 200 ? text.slice(0, 200) + '...' : text));
          }
        }

        if (!res.ok) {
          const msg = (data && (data.message || data.error)) || `HTTP ${res.status}`;
          throw new Error(`Payment failed: ${msg}`);
        }

        if (opts.onComplete) opts.onComplete(data);

      } catch (err) {
        console.error('PayFlow error:', err);
        if (opts.onError) opts.onError(err);
        else alert('PayFlow error: ' + err.message);
      }
    });

    container.innerHTML = '';
    container.appendChild(form);
  }

  // Public API
  window.PayFlow = {
    init: function(opts){
      const target = document.querySelector(opts.selector || '#payflow-widget');
      if (!target) throw new Error('PayFlow: container not found');
      render(target, opts);
    }
  };
})();
