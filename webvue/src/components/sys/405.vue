<template>
	<div class="bg404-back">
		<div class="stars">
			<div class="custom-navbar">
				<div class="navbar-links">
					<ul>
						<li><a href="https://codepen.io/androhani/pen/deOpMZ" target="_blank">Home</a></li>
					</ul>
				</div>
			</div>
			<div class="central-body">
				<img class="image-404" src="../../../static/img/sys/404.svg" width="300px">
				<button type="button" class="btn btn-outline-purple btn-goback" @click='goBack()'>
					<i class='fa fa-arrow-circle-o-left'></i><span>返回</span>
				</button>
			</div>
			<div class="objects">
				<img class="object_rocket" src="../../../static/img/sys/rocket.svg" width="40px">
				<div class="earth-moon">
					<img class="object_earth" src="../../../static/img/sys/earth.svg" width="100px">
					<img class="object_moon" src="../../../static/img/sys/moon.svg" width="80px">
				</div>
				<div class="box_astronaut">
					<img class="object_astronaut" src="../../../static/img/sys/astronaut.svg" width="140px">
				</div>
			</div>
			<div class="glowing_stars">
				<div class="star"></div>
				<div class="star"></div>
				<div class="star"></div>
				<div class="star"></div>
				<div class="star"></div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		name: 'sys405',
		data: function() {
			return {
				showMenu: true
			}
		},
		methods: {
			goBack() {
				// https://codepen.io/androhani/pen/deOpMZ
				// https://codepen.io/salehriaz/pen/erJrZM
				// https://codepen.io/Gogh/pen/eeReMO
				// https://codepen.io/robinselmer/pen/vJjbOZ
				// https://codepen.io/clementGir/pen/PqGyMq
				// https://codepen.io/gavra/pen/upHzg
				// https://codepen.io/igloude/pen/qNNWKr
				this.$root.goBack();
			}
		},
		mounted:function() {
		  this.showMenu = this.$root.getShowMenu();
          this.$root.setShowMenu(false);
          this.$root.setShowBreadcrumb(false);
		},
		destroyed:function() {
          this.$root.setShowMenu(this.showMenu);
          this.$root.setShowBreadcrumb(true);
		}
	}
</script>

<style scoped>
	@-moz-keyframes rocket-movement { 100% {-moz-transform: translate(1200px,-600px);} }
	@-webkit-keyframes rocket-movement {100% {-webkit-transform: translate(1200px,-600px); } }
	@keyframes rocket-movement {96% {transform: translate(1200px,-600px);} 97% {transform: translate(-50px,-600px);} 100% {transform: translate(0,-600px);} }
	@-moz-keyframes spin-earth { 100% { -moz-transform: rotate(-360deg); transition: transform 20s;  } }
	@-webkit-keyframes spin-earth { 100% { -webkit-transform: rotate(-360deg); transition: transform 20s;  } }
	@keyframes spin-earth{ 100% { -webkit-transform: rotate(-360deg); transform:rotate(-360deg); transition: transform 20s; } }

	@-moz-keyframes move-astronaut {
		100% { -moz-transform: translate(-160px, -160px);}
	}
	@-webkit-keyframes move-astronaut {
		100% { -webkit-transform: translate(-160px, -160px);}
	}
	@keyframes move-astronaut{
		100% { -webkit-transform: translate(-160px, -160px); transform:translate(-160px, -160px); }
	}
	@-moz-keyframes rotate-astronaut {
		100% { -moz-transform: rotate(-720deg);}
	}
	@-webkit-keyframes rotate-astronaut {
		100% { -webkit-transform: rotate(-720deg);}
	}
	@keyframes rotate-astronaut{
		100% { -webkit-transform: rotate(-720deg); transform:rotate(-720deg); }
	}

	@-moz-keyframes glow-star {
		40% { -moz-opacity: 0.3;}
		90%,100% { -moz-opacity: 1; -moz-transform: scale(1.2);}
	}
	@-webkit-keyframes glow-star {
		40% { -webkit-opacity: 0.3;}
		90%,100% { -webkit-opacity: 1; -webkit-transform: scale(1.2);}
	}
	@keyframes glow-star{
		40% { -webkit-opacity: 0.3; opacity: 0.3;  }
		90%,100% { -webkit-opacity: 1; opacity: 1; -webkit-transform: scale(1.2); transform: scale(1.2); border-radius: 999999px;}
	}
	.spin-earth-on-hover{
		transition: ease 200s !important;
		transform: rotate(-3600deg) !important;
	}

	.bg404-back{
		position:relative;
		font-family: 'Dosis', sans-serif;
		background-repeat: repeat-x;
		background-size: cover;
		background-position: left top;
		height: 100%;
		overflow: hidden;
		-webkit-user-select: none; /* Safari 3.1+ */
		-moz-user-select: none; /* Firefox 2+ */
		-ms-user-select: none; /* IE 10+ */
		user-select: none; /* Standard syntax */
	}

	.custom-navbar{
		padding-top: 15px;
	}

	.navbar-links{
		display: inline-block;
		float: right;
		margin-right: 15px;
		text-transform: uppercase;
	}

	ul {
		list-style-type: none;
		margin: 0;
		padding: 0;
		/*    overflow: hidden;*/
		display: flex; 
		align-items: center; 
	}

	li {
		float: left;
		padding: 0px 15px;
	}

	li a {
		display: block;
		color: white;
		text-align: center;
		text-decoration: none;
		letter-spacing : 2px;
		font-size: 12px;

		-webkit-transition: all 0.3s ease-in;
		-moz-transition: all 0.3s ease-in;
		-ms-transition: all 0.3s ease-in;
		-o-transition: all 0.3s ease-in;
		transition: all 0.3s ease-in;
	}

	li a:hover {
		color: #ffcb39;
	}

	.btn-request{
		padding: 10px 25px;
		border: 1px solid #FFCB39;
		border-radius: 100px;
		font-weight: 400;
	}

	.btn-request:hover{
		background-color: #FFCB39;
		color: #fff;
		transform: scale(1.05);
		box-shadow: 0px 20px 20px rgba(0,0,0,0.1);
	}

	.btn-goback{
		border: 1px solid #FFCB39;
		border-radius: 100px;
		color:#FFCB39;
		display: block;
		margin: 20px auto 0;
	}

	.btn-goback:hover{
		background-color: #939;
		border-color:#939;
		color: #fff;
		box-shadow: 0px 20px 20px rgba(153,51,153,0.1);
		z-index: 200;
		-webkit-transition: all 0.3s ease-in;
		-moz-transition: all 0.3s ease-in;
		-ms-transition: all 0.3s ease-in;
		-o-transition: all 0.3s ease-in;
		transition: all 0.3s ease-in;
	}

	.central-body{
		/*    width: 100%;*/
		padding: 12% 5% 10% 5%;
		text-align: center;
	}

	.objects img{
		z-index: 90;
		pointer-events: none;
	}

	.object_rocket{
		z-index: 95;
		position: absolute;
		/*transform: translateX(-50px);*/
		top: 75%;
		pointer-events: none;
		animation: rocket-movement 200s linear infinite both running;
	}

	.object_earth{
		position: absolute;
		top: 20%;
		left: 15%;
		z-index: 90;
		/*    animation: spin-earth 100s infinite linear both;*/
	}

	.object_moon{
		position: absolute;
		top: 12%;
		left: 25%;
	}
	.object_astronaut{
		animation: rotate-astronaut 200s infinite linear both alternate;
	}

	.box_astronaut{
		z-index: 110 !important;
		position: absolute;
		top: 60%;
		right: 20%;
		will-change: transform;
		animation: move-astronaut 50s infinite linear both alternate;
	}

	.image-404{
		position: relative;
		z-index: 100;
		pointer-events: none;
	}

	.stars{
		background: url(../../../static/img/sys/stars.svg);
		background-repeat: repeat;
		background-size: contain;
		background-position: left top;
	}

	.glowing_stars .star{
		position: absolute;
		border-radius: 100%;
		background-color: #fff;
		width: 3px;
		height: 3px;
		opacity: 0.3;
		will-change: opacity;
	}

	.glowing_stars .star:nth-child(1){
		top: 80%;
		left: 25%;
		animation: glow-star 2s infinite ease-in-out alternate 1s;
	}
	.glowing_stars .star:nth-child(2){
		top: 20%;
		left: 40%;
		animation: glow-star 2s infinite ease-in-out alternate 3s;
	}
	.glowing_stars .star:nth-child(3){
		top: 25%;
		left: 25%;
		animation: glow-star 2s infinite ease-in-out alternate 5s;
	}
	.glowing_stars .star:nth-child(4){
		top: 75%;
		left: 80%;
		animation: glow-star 2s infinite ease-in-out alternate 7s;
	}
	.glowing_stars .star:nth-child(5){
		top: 90%;
		left: 50%;
		animation: glow-star 2s infinite ease-in-out alternate 9s;
	}

	@media only screen and (max-width: 600px){
		.navbar-links{
			display: none;
		}

		.custom-navbar{
			text-align: center;
		}

		.brand-logo img{
			width: 120px;
		}

		.box_astronaut{
			top: 70%;
		}

		.central-body{
			padding-top: 25%;
		}
	}
</style>
