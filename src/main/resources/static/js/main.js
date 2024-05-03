const { createApp, ref } = Vue

const currencyConvertorAPI = {
    fetch(sourceCurrency, targetCurrency, monetaryValue) {
        const url = new URL('/rates', window.location.origin);
        url.searchParams.append('source_currency', sourceCurrency);
        url.searchParams.append('target_currency', targetCurrency);
        url.searchParams.append('monetary_value', monetaryValue);

        return fetch(url).then(response => response.json());
    }
}

const RateResult = {
    data() {
        return {
            rate: null
        }
    },
    template: '<div>{{rate}}</div>',
    created() {
        currencyConvertorAPI.fetch('EUR', 'USD', 100)
            .then(data => {
                this.rate = data.result;
            })
            .catch(error => console.error('Error:', error));
    }
}

const app = createApp({
    data() {
        return {
            rate: {}
        }
    },
    components: {
        RateResult
    },
    template: `
        <div>
            <RateResult :rate="rate"/>
        </div>
    `
})

app.mount('#app')