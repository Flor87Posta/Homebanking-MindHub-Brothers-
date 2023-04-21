
const {createApp} = Vue


const app = createApp ({

    data(){
        return {
            firstName : null,
            lastName : null,
            accounts: [],
            clients: [],
            loans: [],
            email:null,
            emailRegister: null,
            password:null,
            passwordRegister: null,
            formVisible: false,

        }
    },


    created(){
    //       // Hacer una petición a través de una función loadData
            // this.loadData();
    

            },

        methods: {

        // loadData(){
        //         axios.get("http://localhost:8080/api/clients/1")
        //             .then(response => {
        //                 this.clients=response.data;
        //                 console.log(this.clients)
        //                 this.accounts=this.clients.accounts
        //                 this.loans=this.clients.loans



        //             })
        //             .catch(err => console.log( err ));
        //         },

        // addClient(){
        //     this.postClient();
        // },

        // postClient(){
        //         axios.post("http://localhost:8080/api/clients/1", {
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

        login() {
                axios.post('/api/login',`email=${this.email}&password=${this.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                .then(response => 
                {
                    window.location.href='/web/accounts.html'
                }
                    )
                .catch(function (error) {
                        if (error.response) {
                          // The request was made and the server responded with a status code
                          // that falls out of the range of 2xx
                        console.log(error.response.data);
                        console.log(error.response.status);
                        console.log(error.response.headers);
                        } else if (error.request) {
                          // The request was made but no response was received
                          // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
                          // http.ClientRequest in node.js
                        console.log(error.request);
                        } else {
                          // Something happened in setting up the request that triggered an Error
                        console.log('Error', error.message);
                        }
                        console.log(error.config);
                    });
            },

        logout(){
                axios.post('/api/logout')
                .then(response => console.log('Signed out'))
            },

        showForm() {
                this.formVisible = true;
            },

        register(){

                axios.post('/api/clients', `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.emailRegister}&password=${this.passwordRegister}`)
                .then(response =>{
                    if(response.status === 201){
                        console.log(response.data)
                        this.login()
                    }
                })
                .catch(error => console.log(error))       
            },
        },

})

app.mount('#app')