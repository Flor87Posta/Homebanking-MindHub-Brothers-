
const {createApp} = Vue


const app = createApp ({

    data(){
        return {
            firstName : '',
            lastName : '',
            accounts: [],
            clients : [],
            

        }
    },
    created(){
    //         // Hacer una petición a través de una función loadData
            this.loadData();



            },

        methods: {

        loadData(){
                axios.get("http://localhost:8080/api/clients")
                    .then(response => {
                        this.clients=response.data;
                        console.log(response.data)



                    })
                    .catch(err => console.log( err ));
                },

        addClient(){
            this.postClient();
        },

        postClient(){
                axios.post("http://localhost:8080/api/clients/1", {
                    firstName: this.firstName,
                    lastName: this.lastName,
                    email: this.email,
                    accounts: this.accounts,

                    
                })
                .then(function (response) {
                    this.loadData();
                })
                .catch(function (error) {
                    console.log(error);
                });
            }

            },

})

app.mount('#app')