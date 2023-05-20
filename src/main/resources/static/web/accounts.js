const app = Vue.createApp({
    data() {
      return {
        firstName: '',
        lastName: '',
        accounts: [],
        hidden:[],
        clients: [],
        loans: [],
        createdAccount: false,
        condicion: true,
        accountType: '',
        formVisible: false,
        mostrarFormulario1: true,
        // formVisible2: false,
        mostrarFormulario2: true,
        selectedAccount: null,
        accNumber: '',
        dateIni: '',
        dateEnd: '',
        checked: '',
        searchClient: ''
      };
    },
    created() {
      this.loadData();
    },
    methods: {
      loadData() {
        axios.get("http://localhost:8080/api/clients/current")
          .then(response => {
            this.clients = response.data;
            this.accounts = this.clients.accounts;
            console.log(this.accounts)
            this.loans = this.clients.loans;
            this.condicion = this.accounts.length <= 3;
          })
          .catch(err => console.log(err));
      },
      
        getVisibleAccounts() {
          return this.accounts.filter(account => !account.hidden);
        },

      logout() {
        axios.post('/api/logout')
          .then(response => console.log('Signed out'));
      },
      newAccount() {
        Swal.fire({
          icon: 'warning',
          title: 'You are creating a new Account..¿Are you sure?',
          showCancelButton: true,
          confirmButtonText: 'Yes, create new Account',
          cancelButtonText: 'Cancel',
          timer: 6000,
        }).then((result) => {
          if (result.isConfirmed) {
            axios.post('/api/clients/current/accounts', `accountType=${this.accountType}`)
              .then(response => {
                if (response.status === "200") {
                  this.createdAccount = true;
                  this.loadData();
                  Swal.fire({
                    icon: 'success',
                    title: 'You have a new Account!',
                    showCancelButton: true,
                    confirmButtonText: 'Accepted',
                    cancelButtonText: 'Cancel',
                    timer: 6000,
                  })
                }
              }).then( () => window.location.href="/web/accounts.html")
              .catch(error => Swal.fire({
                icon: 'error',
                title: 'Error',
                text: error.response.data,
                timer: 6000,
              }));
          }
        });
      },
      showForm() {
        this.formVisible = true;
        this.mostrarFormulario1 = false;
      },
      // showForm2() {
      //   this.storeAndShowForm(this.account.number);
      //   this.formVisible2 = true;
      //   this.mostrarFormulario2 = false;
      // },
      storeAndShowForm(accountNumber) {
        this.store(accountNumber);
        this.selectedAccount = accountNumber;
        // this.formVisible2 = true;
        // this.mostrarFormulario2 = false;
      },
      generatePDF(accNumber, dateIni, dateEnd) {
        if (this.dateIni !== "" && this.dateEnd !== "") {
          axios.post("/api/clients/current/export-pdf", `accNumber=${this.accNumber}&dateIni=${this.dateIni} 00:00&dateEnd=${this.dateEnd} 23:55`, {
              responseType: 'blob'
            })
            .then((response) => {
              const url = window.URL.createObjectURL(new Blob([response.data], {
                type: "application/pdf"
              }));
              const link = document.createElement('a');
              link.href = url;
              link.setAttribute('download', `Transaction${this.accNumber}.pdf`);
              document.body.appendChild(link);
              link.click();
            })
            .catch(err => console.log(err));
        } else {
          Swal.fire(
            'Select two dates to filter!',
            'warning'
          );
        }
      },
      store(accountNumber) {
        this.accNumber = accountNumber;
        return this.accNumber;
      },
    },
  });
  
  app.mount('#app');