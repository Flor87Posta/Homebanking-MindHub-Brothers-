
const {createApp} = Vue


const app = createApp ({
    //before Created: se ejecuta justo después de que se instancia una nueva instancia de Vue, pero antes de que se
// hayan creado las propiedades reactivas y se haya compilado la plantilla.

    data(){
        return {

            account: [],
            transactions: [],
            valorID: (new URLSearchParams(location.search)).get("id"),

            

        }
    },
    created(){ //Created: Es el momento adecuado para realizar operaciones asíncronas, como hacer peticiones a APIs.
         // Hacer una petición a través de una función loadData
            this.loadData();
            this.arraySorted(this.transactions);
            



            },

        methods: {

        loadData(){
                axios.get("http://localhost:8080/api/accounts/"+ this.valorID)
                    .then(response => {
                        this.account=response.data;
                        console.log(this.account);
                        this.transactions=this.account.transactions;
                        


                    })
                    .catch(err => console.log( err ));
                },

        arraySorted() {
                    if (this.transactions != "") {
                        return this.transactions.sort((a, b) => b.transactionDate - a.transactionDate)
                    }
                
            },
        }
})
//before Mount
app.mount('#app') //mounted: se ejecuta justo después de que se monte la instancia de Vue en el DOM. Es el momento adecuado
// para realizar operaciones que requieran acceder al DOM, como enlazar eventos de DOM

// beforeUpdate: se ejecuta justo antes de que se actualice la instancia de Vue con los cambios en el estado de la aplicación.

//updated: se ejecuta justo después de que se actualice la instancia de Vue con los cambios en el estado de la aplicación.

// beforeUnmount: se ejecuta justo antes de que se desmonte la instancia de Vue.

//unmounted: se ejecuta justo después de que se demonta la instancia de Vue.

