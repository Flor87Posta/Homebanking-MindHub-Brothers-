
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
            createdCard: false,
            typeCardCreated: null,
            colorCard: null,

            

        }
    },
    created(){
    //         // Hacer una petición a través de una función loadData
            this.loadData();
    

            },

        methods: {

        loadData(){
                axios.get("http://localhost:8080/api/clients/current")
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



                    })
                    .catch(err => console.log( err ));
                },

        // addClient(){
        //     this.postClient();
        // },

        // postClient(){
        //         axios.post("http://localhost:8080/api/clients/current", {
        //             firstName: this.firstName,
        //             lastName: this.lastName,
        //             email: this.email,
        //             accounts: this.accounts,

                    
        //         })
        //         .then(function (response) {
        //             this.loadData();
        //         })
        //         .catch(function (error) {
        //             console.log(error);
        //         });
        //     },

            logOut(){
                axios.post('/api/logout')
                .then(response => console.log('Signed out'))
            },

            newCard() {
                console.log("hola");
                axios.post('/api/clients/current/cards',`typeCard=${this.typeCardCreated}&color=${this.colorCard}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        if (response.status == "201") {
                            console.log(response),
                            window.location.href='/web/cards.html',
                                this.createdCard = true,
                                this.loadData()
                        }
                    })
                    .catch(error => {
                        console.log(error);
                        if (error.code == "ERR_BAD_REQUEST") {
                            console.log(error)
                        }
                    })
            },

            },

})

app.mount('#app')