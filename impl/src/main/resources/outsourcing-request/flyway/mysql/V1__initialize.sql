create table osr_outsourcing_request (
	id binary(16) not null,
	accepted_date datetime,
	accepter_id varchar(50),
	canceled_date datetime,
	code varchar(20),
	committed_date datetime,
	completed_date datetime,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	due_date datetime,
	item_id binary(16),
	item_spec_code varchar(20),
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	process_id binary(16),
	project_id binary(16),
	quantity decimal(19,2),
	receive_site_id binary(16),
	receive_station_id binary(16),
	receiver_id varchar(50),
	rejected_date datetime,
	rejected_reason varchar(50),
	remark varchar(50),
	requester_id varchar(50),
	spare_quantity decimal(19,2),
	status varchar(20),
	supplier_id varchar(50),
	primary key (id)
) engine=InnoDB;

create table osr_outsourcing_request_material (
	id binary(16) not null,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	estimated_supply_date datetime,
	item_id binary(16),
	item_spec_code varchar(20),
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	quantity decimal(19,2),
	remark varchar(50),
	request_id binary(16),
	supplier_id varchar(50),
	primary key (id)
) engine=InnoDB;

create index IDX4xicy22hrgdd0okv1b9oswfm6
	on osr_outsourcing_request_material (request_id);
