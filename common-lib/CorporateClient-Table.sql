DROP TABLE public.corporate_client;
CREATE TABLE public.corporate_client (
	corporate_client_id serial NOT NULL,
	corporate_client_name varchar(100) NULL,
	corporate_client_address varchar(200) NULL,
	corporate_client_phone_number varchar NULL,
	corporate_client_email varchar(100) NULL,
	corporate_client_provider varchar(100) NULL,
	corporate_client_provider_id varchar(20) NULL,
	created_date timestamp NULL,
	created_by varchar(50) NULL,
	update_date timestamp NULL,
	update_by varchar(50) NULL,	
	CONSTRAINT corporate_client_pk PRIMARY KEY (corporate_client_id)
)
WITH (
	OIDS=FALSE
) ;

-- Permissions

ALTER TABLE public.corporate_client OWNER TO ninelives;
GRANT INSERT, SELECT, UPDATE, REFERENCES(created_date) ON public.corporate_client TO ninelives;

insert into corporate_client ( corporate_client_name, corporate_client_address, 
corporate_client_email, corporate_client_phone_number, corporate_client_provider, 
corporate_client_provider_id,created_date, created_by) values 
('PT. XYZ Jaya','Kemang Utara jakarta selatan', 'xyz.pt@gmail.com','021-7808888','aswata','ASW-XYZPT-01',now(),'admin')
