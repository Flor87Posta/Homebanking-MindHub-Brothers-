
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
            requestedNumber: "",
            numberCreditCard: [],
            numberDebitCard:[],
            formVisible: false,
            mostrarFormulario1:true,

            

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
                        this.numberCreditCard = this.creditCards.number
                        console.log(this.numberCreditCard)
                        this.numberDebitCard = this.debitCards.number
                        console.log(this.numberDebitCard)



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

            logout(){
                axios.post('/api/logout')
                .then(response => console.log('Signed out'))
            },

            newCard() {
                Swal.fire({
                    icon: 'warning',
                    title: 'You are creating a new Card..¿Are you sure?',
                    showCancelButton: true,
                    confirmButtonText: 'Yes, create new Card',
                    cancelButtonText: 'Cancel',
                    timer: 6000,
                }).then((result) => {
                    if (result.isConfirmed) {
                        axios.post('/api/clients/current/cards',`typeCard=${this.typeCardCreated}&color=${this.colorCard}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                            .then(response => {
                                if (response.status == "201") {
                                    console.log(response),
                                    window.location.href='/web/cards.html',
                                    this.createdCard = true,
                                    this.loadData()
                                    Swal.fire({
                                        icon: 'Success!',
                                        title: 'You have a new Card!',
                                        showCancelButton: true,
                                        confirmButtonText: 'Accepted',
                                        cancelButtonText: 'Cancel',
                                        timer: 6000,
                                    })
                                }  
                            })
                            .catch(error => Swal.fire({
                                icon: 'error',
                                title: 'Error',
                                text: error.response.data,
                                timer: 6000,
                            }))
                    }
                })
            },

            deleteCard() {
                Swal.fire({
                    icon: 'warning',
                    title: 'You are deleting a Card..¿Are you sure?',
                    showCancelButton: true,
                    confirmButtonText: 'Yes, delete Card',
                    cancelButtonText: 'Cancel',
                    timer: 6000,
                }).then((result) => {
                    if (result.isConfirmed) {
                axios.post('/api/clients/current/delete-card', `requestedNumber=${this.requestedNumber}`)
                .then(response => {
                    if (response.status == "201") {
                        console.log(response),
                        window.location.href='/web/cards.html',
                        this.loadData()
                        Swal.fire({
                            icon: 'Success!',
                            title: 'You Delete a Card!',
                            showCancelButton: true,
                            confirmButtonText: 'Accepted',
                            cancelButtonText: 'Cancel',
                            timer: 6000,
                        })
                    }  
                })
                .catch(error => Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: error.response.data,
                    timer: 6000,
                }))
                    }
                        })
                            },

                showForm() {
                            this.formVisible = true;
                            this.mostrarFormulario1=false;
                            },

            },

})

app.mount('#app')