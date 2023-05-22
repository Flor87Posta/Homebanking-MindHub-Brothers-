
const {createApp} = Vue


const app = createApp ({

    data(){
        return {
            firstName : '',
            lastName : '',
            accounts: [],
            clients: [],
            loans: [],
            cards: [],
            debitCards: [],
            creditCards: [],
            condicion: true,

            

        }
    },
    created(){
    //         // Hacer una petición a través de una función loadData
            this.loadData();
    

            },

        methods: {

        loadData(){
                axios.get("/api/clients/current")
                    .then(response => {
                        this.clients=response.data;
                        console.log(this.clients)
                        this.accounts=this.clients.accounts
                        this.loans=this.clients.loans
                        this.cards=this.clients.cards
                        this.debitCards = this.cards.filter(card => card.typeCard === "DEBIT")
                        console.log(this.debitCards)
                        this.creditCards = this.cards.filter(card => card.typeCard === "CREDIT")
                        console.log(this.creditCards)
                        this.condicion=(this.debitCards<=3)&&(this.creditCards<=3)



                    })
                    .catch(err => console.log( err ));
                },




            logout(){
                axios.post('/api/logout')
                .then(response => console.log('Signed out'))
            },

            },

})

app.mount('#app')