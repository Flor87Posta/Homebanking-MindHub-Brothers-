
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
            mostrarFormulario1:true,

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
                {   if(this.email =="florys_211@hotmail.com"){
                    window.location.replace('/manager.html'); /* window.location.replace('http://localhost:8080/h2-console');*/
                } else {
                    window.location.href='/web/accounts.html';
                    Swal.fire({
                        icon: 'success',
                        title: 'Login successful!',
                        showCancelButton: true,
                        confirmButtonText: 'Ok',
                        cancelButtonText: 'Cancel',
                        timer: 6000,
                    });}
          
                }
                    )
                    .catch(function (error) {
                        if (error.response) {
                            Swal.fire({
                                icon: 'error',
                                title: 'Login failed!',
                                text: error.response.data,
                                timer: 6000,
                            });
                        }
                    })
            },

            loginRegister() { //para clientes que se registran desde cero, como los mails y password se llaman distintos..

                axios.post('/api/login',`email=${this.emailRegister}&password=${this.passwordRegister}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                .then(response => 
                {   window.location.href='/web/accounts.html';
                    Swal.fire({
                        icon: 'success',
                        title: 'Login successful!',
                        showCancelButton: true,
                        confirmButtonText: 'Ok',
                        cancelButtonText: 'Cancel',
                        timer: 6000,
                    });
                }
                    )
                .catch(function (error) {
                        if (error.response) {
                            Swal.fire({
                                icon: 'error',
                                title: 'Login failed!',
                                text: error.response.data,
                                timer: 6000,
                            });
                        }
                    });
            },

        logout(){
                axios.post('/api/logout')
                .then(response => console.log('Signed out'))
            },

        showForm() {
                this.formVisible = true;
                this.mostrarFormulario1=false;
            },

        register(){

                axios.post('/api/clients', `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.emailRegister}&password=${this.passwordRegister}`)
                .then(response =>{

                    console.log("hola")
                    if(response.status === 201){
                        console.log(response.data)
                        this.loginRegister()
                    }
                })
                .catch(error => console.log(error))       
            },

            //acá le agregue las alertas y no me funciona...

            // register(){
            //     Swal.fire({
            //         icon: 'warning',
            //         title: 'You are creating a new User..¿Are you sure?',
            //         showCancelButton: true,
            //         confirmButtonText: 'Yes, create new Account',
            //         cancelButtonText: 'Cancel',
            //         timer: 6000,
            //     }).then((result) => {
            //         if (result.isConfirmed) {
            //             axios.post('/api/clients', `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.emailRegister}&password=${this.passwordRegister}`)
            //     .then(response =>{
            //                     if (response.status == "201") {
            //                         this.loginRegister()
            //                             Swal.fire({
            //                                 icon: 'success',
            //                                 title: 'Congratulations! You are a client now!',
            //                                 showCancelButton: true,
            //                                 confirmButtonText: 'Accepted',
            //                                 cancelButtonText: 'Cancel',
            //                                 timer: 6000,
            //                             })
            //                     }
            //                 })
            //                 .catch(error => Swal.fire({
            //                     icon: 'error',
            //                     title: 'Error',
            //                     text: error.response.data,
            //                     timer: 6000,
            //                 }))
            //         }
            //     })
            // },


        },

})

app.mount('#app')