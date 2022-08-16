<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- SEO Meta Tags -->
<meta name="description" content="Your description">
<meta name="author" content="Your name">


<!-- Webpage Title -->
<title>WebDev</title>

<!-- Styles -->
<link type="text/css"
	href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700&display=swap"
	rel="stylesheet">
<link type="text/css" href="/css/bootstrap.min.css" rel="stylesheet">
<link type="text/css" href="/css/fontawesome-all.min.css"
	rel="stylesheet">
<link type="text/css" href="/css/aos.min.css" rel="stylesheet">
<link type="text/css" href="/css/swiper.css" rel="stylesheet">
<link type="text/css" href="/css/style.css" rel="stylesheet">

<!-- Favicon -->
<link rel="icon" href="./assets/images/favicon.png">
</head>

<body>
	<!-- Navigation -->
	<nav id="navbar" class="navbar navbar-expand-lg fixed-top navbar-dark"
		aria-label="Main navigation">
		<div class="container">
			<!-- Image Logo -->
			<!-- <a class="navbar-brand logo-image" href="index.html"><img src="images/logo.svg" alt="alternative"></a> -->
			<!-- Text Logo - Use this if you don't have a graphic logo -->
			<a class="navbar-brand logo-text" href="/">WebDev</a>
			<button class="navbar-toggler p-0 border-0" type="button"
				id="navbarSideCollapse" aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<!-- end of navbar-collapse -->
		</div>
		<!-- end of container -->
	</nav>
	<!-- end of navbar -->
	<!-- end of navigation -->
	<!-- reg -->

	<section class="reg d-flex align-items-center py-5 " id="reg">
		<div class="container-fluid text-light">
			<div class="row justify-content-center">
				<div
					class="col-lg-6 d-flex justify-content-center  boarder p-4  align-items-center px-lg-5"
					data-aos="fade-right">
					<div style="width: 90%">
						<div class="text-center text-lg-start py-4 pt-lg-0">
							<h2 class="py-2">Register</h2>
						</div>
						<div>
							<form:form action="/register" method="post"
								modelAttribute="newUser">

								<div class="row">
									<div class="col-lg-6">
										<div class="form-group py-2">
											<form:input path="firstName" type="text"
												class="form-control form-control-input"
												cssClass="form-control"
												cssErrorClass="form-control is-invalid"
												placeholder="Enter first name" />
											<small> <form:errors path="firstName"
													class="text-danger" />
											</small>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group py-2">
											<form:input path="lastName" type="text"
												class="form-control form-control-input"
												cssClass="form-control"
												cssErrorClass="form-control is-invalid"
												placeholder="Enter last name" />
											<small> <form:errors path="lastName"
													class="text-danger" />
											</small>
										</div>
									</div>
								</div>
								<div class="form-group py-1">
									<form:input path="email" type="text"
										class="form-control form-control-input"
										cssClass="form-control"
										cssErrorClass="form-control is-invalid"
										placeholder="Enter email" />
									<small> <form:errors path="email" class="text-danger" />
									</small>
								</div>
								<div class="form-group py-1">
									<form:input path="password" type="password"
										class="form-control form-control-input"
										cssClass="form-control"
										cssErrorClass="form-control is-invalid"
										placeholder="Enter password" />
									<small> <form:errors path="password"
											class="text-danger" />
									</small>
								</div>
								<div class="form-group py-1">
									<form:input path="confirmPassword" type="password"
										class="form-control form-control-input"
										cssClass="form-control"
										cssErrorClass="form-control is-invalid"
										placeholder="Confirm password" />
									<small> <form:errors path="confirmPassword"
											class="text-danger" />
									</small>
								</div>
								<div class="my-3">
									<button class="btn" type="submit">Register</button>
									<a href="/log" class="m-3">already have an account? login</a>
								</div>
							</form:form>
						</div>

					</div>
					<!-- end of div -->
				</div>

				<!-- end of col -->
			</div>
			<!-- end of row -->
		</div>
		<!-- end of container -->
	</section>
	<!-- end of reg -->

	<!-- Scripts -->
	<script type="text/javascript" src="/js/bootstrap.min.js"></script>
	<!-- Bootstrap framework -->
	<script type="text/javascript" src="/js/purecounter.min.js"></script>
	<!-- Purecounter counter for statistics numbers -->
	<script type="text/javascript" src="/js/swiper.min.js"></script>
	<!-- Swiper for image and text sliders -->
	<script type="text/javascript" src="/js/aos.js"></script>
	<!-- AOS on Animation Scroll -->
	<script type="text/javascript" src="/js/script.js"></script>
	<!-- Custom scripts -->
</body>

</html>