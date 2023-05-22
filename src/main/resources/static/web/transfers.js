const { createApp } = Vue
createApp({
    data() {
        return {
            currentId: null,
          client: null,
          destination:null,
          description:"",
          originAccNumber:null,
          destinationAccNumber:null,
          amount:null,
          filteredAccounts: [],
          invalidTransaction:false,
          transactionError:"",
          transactionSuccess:false,
          clientAccounts:[]
          
        }
    },

    created() {
       this.loadData()
    },

    methods: {
        loadData() {
            this.isLoading()
            axios
                .get(`/api/clients/current`)
                .then(data => {
                    console.log(data);
                    this.client = data.data;
                    console.log(this.client);
                    this.sortAccounts()
                }
                )
                .catch(error => {
                    console.log(error);
                })
                this.finishedLoading()
        },

        makeTransaction() {
          Swal.fire({
            icon: 'warning',
            title: 'You are making a new Transfer..¿Are you sure?',
            showCancelButton: true,
            confirmButtonText: 'Yes, make new Transfer',
            cancelButtonText: 'Cancel',
            timer: 6000,
        }).then((result) => {
            if (result.isConfirmed) {
            axios.post('/api/clients/current/transactions',`originAccNumber=${this.originAccNumber}&destinationAccNumber=${this.destinationAccNumber}&amount=${this.amount}&description=${this.description}`)
            .then(response => {
                Swal.fire({
                    icon: 'success',
                    title: 'You have made a new Transfer!',
                    showCancelButton: true,
                    confirmButtonText: 'Accepted',
                    cancelButtonText: 'Cancel',
                    timer: 6000,
                })    
                window.location.href='/web/accounts.html';
                this.transactionSuccess=true;
                
            })
            .catch(error => Swal.fire({
                icon: 'error',
                title: 'Error',
                text: error.response.data,
                timer: 6000,
            }))

            }
            this.destination=null;
            this.description="";
            this.originAccNumber=null;
            this.destinationAccNumber=null;
            this.amount=null;
          })
          },



        filterAccounts() {
            this.filteredAccounts = this.client.accounts.filter(account => account.number !== this.originAccNumber)
          },



        isLoading() {
            this.loading = true;
        },
        finishedLoading() {
            this.loading = false;
        },


        sortAccounts() {
            let sortedAccounts = this.client.accounts.sort((acc1, acc2) => acc1.id - acc2.id)
            this.clientAccounts = sortedAccounts
        },

        toggleToggler() {
            const toggler = document.getElementById("toggler");
            toggler.classList.toggle('opened'); toggler.setAttribute('aria-expanded', toggler.classList.contains('opened'))
        },
        toggleTogglerLarge() {
            const toggler = document.getElementById("togglerLarge");
            toggler.classList.toggle('opened'); toggler.setAttribute('aria-expanded', toggler.classList.contains('opened'))
        },
        toggleTogglerMain() {
            const toggler = document.getElementById("toggler");
            const offCanvas = document.getElementById("offCanvas");
            if (offCanvas.classList.contains('is-open')) {
                toggler.classList.toggle('opened'); toggler.setAttribute('aria-expanded', toggler.classList.contains('opened'))
            }

        },

        logout(){
            axios.post('/api/logout')
            .then(response => console.log('Signed out'))
        },
        
    }
}).mount("#app")