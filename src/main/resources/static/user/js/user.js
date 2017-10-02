var base = document.getElementById("base").href;
var userVM = new Vue({
		el: '#userVM',
		data:{
			pageList: [],
			allCount: -1,
			pageNumber: 1,
			pageSize: 2,
			keyword: '',
			clicked: -1,
			isCheckAll: false,
			userId:'',
			username:'',
			password:'',
			indeterminate: false,
			addUserDialog: false,
			showDelUserModel: false,
			ids:{}
		},
		mounted: function(){
			
		},
		created: function(){
			this.getUserPageList();
			
		},
		methods:{
			cleanSearchInput: function() {
				this.keyword='';
				this.getUserSearch();
			},
			getUserSearch: function(){
				
			},
			pageChange: function(newPage) {
				this.pageNumber = newPage;
				this.getUserPageList();
		  	},
			changeCheck: function(event) {
				var checkedCount = $("input[name='userCheckbox']:checked").length;
				var totalCount = $("input[name='userCheckbox']").length;
				if(checkedCount > 0 && checkedCount < totalCount) {
					this.indeterminate = true;
				} else {
					this.indeterminate = false;
				}
				if(checkedCount==totalCount) {
					this.isCheckAll = true;
				} else {
					this.isCheckAll = false;
				}
				$(event.target).parent().toggleClass("ui-checkbox-checked");
			},
			checkAll: function() {
				var checks = document.getElementsByName("userCheckbox");
				for(var i=0 ; i< checks.length; i++)
				{
					checks[i].checked = this.isCheckAll;
					if(this.isCheckAll) {
						$(checks[i]).parent().addClass("ui-checkbox-checked");
					} else {
						$(checks[i]).parent().removeClass("ui-checkbox-checked");
					}
				}
				this.indeterminate = false;
			},
			toSaveUserDialog:function(userId){
				this.addUserDialog = true;
				this.userId = userId;
			},
			saveUser:function(){
				$.ajax({
					url : "/user/saveUser",
					type : "POST",
					dateType : "json",
					data : {
						"userName": this.username,
						"password": this.password,
						"id":this.userId
					},
					success : function(resultData) {
						if(resultData && resultData.code == 200){
							Vue.prototype.$Message.success("操作成功");
							if(resultData.data){
								userVM.userId = resultData.data;
							}
							this.getUserPageList();
							
						}else{
							Vue.prototype.$Message.warning("操作失败");
						}
						
						this.addUserDialog = false;
						
					}
				});
			},
			toDeleteUserDialog:function(userId){
				this.showDelUserModel = true;
				var idArrays = new Array();
				idArrays.push(userId);
				this.ids = idArrays;
				
			},
			toBatchDel:function(){
				this.showDelUserModel = true;
				var idArrays = new Array();
				idArrays.push($("input[name='userCheckbox']:checked").val());
				this.ids = idArrays;
			},
			deleteUser:function(){
				$.ajax({
					url : "/user/deleteUser",
					type : "POST",
					dateType : "json",
					data : {
						"ids": this.ids
						
					},
					success : function(resultData) {
						if(resultData && resultData.code == 200){
							Vue.prototype.$Message.success("操作成功");
							
						}
						
					}
				});
			}
			},
			getUserPageList:function(){
				$.ajax({
					url : "/user/getUserPageList",
					type : "POST",
					dateType : "json",
					data : {
						"pageNumber": this.pageNumber,
						"pageSize": this.pageSize,
						"keyword": this.keyword
					},
					success : function(resultData) {
						if(resultData && resultData.code == 200){
							if(resultData.data){
								userVM.pageList = resultData.data.list;
								userVM.allCount = resultData.data.total;
							}
							
						}
						
					}
				});
			}
		
			
		}
		
});