<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html lang="en">

<head>
<meta charset="utf-8">
<title>WebDev</title>
<meta content="width=device-width, initial-scale=1.0" name="viewport">
<meta content="" name="keywords">
<meta content="" name="description">

<!-- Favicon -->
<link href="img/favicon.ico" rel="icon">

<!-- Google Web Fonts -->
<link rel="preconnect" href="https://fonts.gstatic.com">
<link
	href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700&display=swap"
	rel="stylesheet">
<!-- Icon Font Stylesheet -->
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css"
	rel="stylesheet">

<!-- Libraries Stylesheet -->
<link href="lib/owlcarousel/assets/owl.carousel.min.css"
	rel="stylesheet">
<link href="lib/tempusdominus/css/tempusdominus-bootstrap-4.min.css"
	rel="stylesheet" />

<!-- Customized Bootstrap Stylesheet -->
<link href="css/dashboard//bootstrap.min.css" rel="stylesheet">

<!-- Template Stylesheet -->
<link href="css/dashboard/style.css" rel="stylesheet">
</head>

<body>

	<div class="container-fluid position-relative d-flex p-0">
		<!-- Sidebar Start -->
		<div class="sidebar pe-4 pb-3">
			<nav class="navbar bg-secondary navbar-dark">
				<h3>
					<a class="navbar-brand mx-4 mb-3">WebDev</a>
				</h3>
				<div class="navbar-nav w-100">
					<a href="/userdashboard" class="nav-item nav-link active">User
						Dashboard</a>
				</div>
			</nav>
		</div>
		<!-- Sidebar End -->

		<!-- Content Start -->
		<div class="content">
			<c:if test="${error != null}">
				<div class="alert alert-danger mt-2" style="color: red;"
					role="alert">
					<c:out value="${error}" />
				</div>
			</c:if>
			<!-- Navbar Start -->
			<nav
				class="navbar navbar-expand bg-secondary navbar-dark sticky-top px-4 py-0">
				<form action="/findbycity" method="post"
					class="d-none d-md-flex ms-4">
					<input class="form-control bg-dark border-0" name="city"
						type="search" placeholder="Search">
				</form>
				<div class="navbar-nav align-items-center ms-auto">
					<div class="nav-item dropdown">
						<a href="#" class="nav-link dropdown-toggle"
							data-bs-toggle="dropdown"> <img
							class="rounded-circle me-lg-2" src="/assets/images/icon.jpg"
							alt="" style="width: 40px; height: 40px;"> <span
							class="d-none d-lg-inline-flex"><c:out
									value="${loggedFname}" /> <c:out value="${loggedLname}" /></span>
						</a>
						<div
							class="dropdown-menu dropdown-menu-end bg-secondary border-0 rounded-0 rounded-bottom m-0">
							<a href="/account" class="dropdown-item">My Account</a> <a
								href="/logout" class="dropdown-item">Log Out</a>
						</div>
					</div>
				</div>
			</nav>
			<!-- Navbar End -->
			<!-- Recent Jobs Start -->
			<div class="container-fluid pt-4 px-4">
				<div class="bg-secondary text-center rounded p-4">
					<div class="d-flex align-items-center justify-content-between mb-4">
						<h6 class="mb-0">Recent Jobs</h6>
					</div>
					<div class="table-responsive">
						<table
							class="table text-start align-middle table-bordered table-hover mb-0">
							<thead>
								<tr style="color: #0ee951;">
									<th scope="col">Job Title</th>
									<th scope="col">Job Description</th>
									<th scope="col">Location</th>
									<th scope="col">Required Experience</th>
									<th scope="col">Wanted GPA</th>
									<th scope="col">Apply</th>
								</tr>
							</thead>
							<tbody class="text-white">
								<c:forEach var="job" items="${jobs}">
									<td>
									<tr>
										<td><c:out value="${job.title}" /></td>
										<td><c:out value="${job.description}" /></td>
										<td><c:out value="${job.location}" /></td>
										<td><c:out value="${job.experience_Required}" /></td>
										<td><c:out value="${job.gpa_wanted}" /></td>
										<td><form:form method="post"
												action="/jobs/apply/${job.id}" modelAttribute="apply">
												<input type="submit" class="btn2" value="Apply">
											</form:form></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="container-fluid pt-4 px-4">
				<div class="bg-secondary text-center rounded p-4">
					<div class="d-flex align-items-center justify-content-between mb-4">
						<h6 class="mb-0">Requested Jobs</h6>
					</div>
					<div class="table-responsive">
						<table
							class="table text-start align-middle table-bordered table-hover mb-0">
							<thead>
								<tr style="color: #0ee951;">
									<th scope="col">Job ID</th>
									<th scope="col">Status</th>
								</tr>
							</thead>
							<tbody class="text-white">
								<c:forEach var="req" items="${requestedJobs}">
									<tr>
										<td><c:out value="${req.jobOffer.title}" /></td>
										<td><c:out value="${req.status}" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!-- Recent Jobs End -->
		</div>
		<!-- Content End -->
	</div>
	<!-- JavaScript Libraries -->
	<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
	<script src="lib/chart/chart.min.js"></script>
	<script src="lib/easing/easing.min.js"></script>
	<script src="lib/waypoints/waypoints.min.js"></script>
	<script src="lib/owlcarousel/owl.carousel.min.js"></script>
	<script src="lib/tempusdominus/js/moment.min.js"></script>
	<script src="lib/tempusdominus/js/moment-timezone.min.js"></script>
	<script src="lib/tempusdominus/js/tempusdominus-bootstrap-4.min.js"></script>

	<!-- Template Javascript -->
	<script src="js/main.js"></script>
</body>

</html>