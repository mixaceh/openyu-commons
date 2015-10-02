package org.openyu.commons.cassandra.thrift.impl;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.thrift.AuthenticationException;
import org.apache.cassandra.thrift.AuthenticationRequest;
import org.apache.cassandra.thrift.AuthorizationException;
import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.CfDef;
import org.apache.cassandra.thrift.CfSplit;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.Compression;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.CounterColumn;
import org.apache.cassandra.thrift.CqlPreparedResult;
import org.apache.cassandra.thrift.CqlResult;
import org.apache.cassandra.thrift.IndexClause;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KeyRange;
import org.apache.cassandra.thrift.KeySlice;
import org.apache.cassandra.thrift.KsDef;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SchemaDisagreementException;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.TokenRange;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.cassandra.thrift.CtSession;
import org.openyu.commons.cassandra.thrift.ex.CtSessionException;
import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.openyu.commons.util.Delegateable;

public class CtSessionImpl extends BaseModelSupporter implements
		CtSession {

	private static final long serialVersionUID = -4057203898058997333L;

	private static transient Logger LOGGER = LoggerFactory
			.getLogger(CtSessionImpl.class);

	protected boolean closed = false;

	protected TTransport ttransport;

	protected transient CtSessionFactoryImpl factory;

	private Cassandra.Client delegate;

	public CtSessionImpl(
			CtSessionFactoryImpl factory, TTransport ttransport) {
		this.factory = factory;
		this.ttransport = ttransport;
		//
		if (ttransport instanceof Delegateable) {
			@SuppressWarnings("unchecked")
			Delegateable<Cassandra.Client> targetable = (Delegateable<Cassandra.Client>) ttransport;
			this.delegate = targetable.getDelegate();
		}
	}

	public boolean isClosed() {
		return this.closed;
	}

	protected void errorIfClosed() {
		if (this.closed)
			throw new CtSessionException(
					"CassandraThriftSession was already closed");
	}

	public TTransport close() throws CtSessionException {
		if (isClosed()) {
			throw new CtSessionException(
					"CassandraThriftSession was already closed");
		}
		this.closed = true;
		//
		try {
			this.factory.closeSession();
			return ttransport;
		} catch (Exception ex) {
			throw new CtSessionException(
					"Cannot close CassandraThriftSession");
		}
	}

	public boolean isConnected() {
		return (!isClosed()) && (this.ttransport != null)
				&& (this.ttransport.isOpen());
	}

	public void login(AuthenticationRequest paramAuthenticationRequest)
			throws AuthenticationException, AuthorizationException, TException {
		errorIfClosed();
		try {
			this.delegate.login(paramAuthenticationRequest);
		} catch (AuthenticationException e) {
			handleException(e);
		} catch (AuthorizationException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void set_keyspace(String paramString)
			throws InvalidRequestException, TException {
		errorIfClosed();
		try {
			this.delegate.set_keyspace(paramString);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public ColumnOrSuperColumn get(ByteBuffer paramByteBuffer,
			ColumnPath paramColumnPath, ConsistencyLevel paramConsistencyLevel)
			throws InvalidRequestException, NotFoundException,
			UnavailableException, TimedOutException, TException {
		errorIfClosed();
		try {
			return this.delegate.get(paramByteBuffer, paramColumnPath,
					paramConsistencyLevel);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (NotFoundException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<ColumnOrSuperColumn> get_slice(ByteBuffer paramByteBuffer,
			ColumnParent paramColumnParent, SlicePredicate paramSlicePredicate,
			ConsistencyLevel paramConsistencyLevel)
			throws InvalidRequestException, UnavailableException,
			TimedOutException, TException {
		errorIfClosed();
		try {
			return this.delegate.get_slice(paramByteBuffer, paramColumnParent,
					paramSlicePredicate, paramConsistencyLevel);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public int get_count(ByteBuffer paramByteBuffer,
			ColumnParent paramColumnParent, SlicePredicate paramSlicePredicate,
			ConsistencyLevel paramConsistencyLevel)
			throws InvalidRequestException, UnavailableException,
			TimedOutException, TException {
		errorIfClosed();
		try {
			return this.delegate.get_count(paramByteBuffer, paramColumnParent,
					paramSlicePredicate, paramConsistencyLevel);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return 0;
	}

	public Map<ByteBuffer, List<ColumnOrSuperColumn>> multiget_slice(
			List<ByteBuffer> paramList, ColumnParent paramColumnParent,
			SlicePredicate paramSlicePredicate,
			ConsistencyLevel paramConsistencyLevel)
			throws InvalidRequestException, UnavailableException,
			TimedOutException, TException {
		errorIfClosed();
		try {
			return this.delegate.multiget_slice(paramList, paramColumnParent,
					paramSlicePredicate, paramConsistencyLevel);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public Map<ByteBuffer, Integer> multiget_count(List<ByteBuffer> paramList,
			ColumnParent paramColumnParent, SlicePredicate paramSlicePredicate,
			ConsistencyLevel paramConsistencyLevel)
			throws InvalidRequestException, UnavailableException,
			TimedOutException, TException {
		errorIfClosed();
		try {
			return this.delegate.multiget_count(paramList, paramColumnParent,
					paramSlicePredicate, paramConsistencyLevel);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<KeySlice> get_range_slices(ColumnParent paramColumnParent,
			SlicePredicate paramSlicePredicate, KeyRange paramKeyRange,
			ConsistencyLevel paramConsistencyLevel)
			throws InvalidRequestException, UnavailableException,
			TimedOutException, TException {
		errorIfClosed();
		try {
			return this.delegate.get_range_slices(paramColumnParent,
					paramSlicePredicate, paramKeyRange, paramConsistencyLevel);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<KeySlice> get_paged_slice(String paramString,
			KeyRange paramKeyRange, ByteBuffer paramByteBuffer,
			ConsistencyLevel paramConsistencyLevel)
			throws InvalidRequestException, UnavailableException,
			TimedOutException, TException {
		errorIfClosed();
		try {
			return this.delegate.get_paged_slice(paramString, paramKeyRange,
					paramByteBuffer, paramConsistencyLevel);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<KeySlice> get_indexed_slices(ColumnParent paramColumnParent,
			IndexClause paramIndexClause, SlicePredicate paramSlicePredicate,
			ConsistencyLevel paramConsistencyLevel)
			throws InvalidRequestException, UnavailableException,
			TimedOutException, TException {
		errorIfClosed();
		try {
			return this.delegate.get_indexed_slices(paramColumnParent,
					paramIndexClause, paramSlicePredicate,
					paramConsistencyLevel);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public void insert(ByteBuffer paramByteBuffer,
			ColumnParent paramColumnParent, Column paramColumn,
			ConsistencyLevel paramConsistencyLevel)
			throws InvalidRequestException, UnavailableException,
			TimedOutException, TException {
		errorIfClosed();
		try {
			this.delegate.insert(paramByteBuffer, paramColumnParent,
					paramColumn, paramConsistencyLevel);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void add(ByteBuffer paramByteBuffer, ColumnParent paramColumnParent,
			CounterColumn paramCounterColumn,
			ConsistencyLevel paramConsistencyLevel)
			throws InvalidRequestException, UnavailableException,
			TimedOutException, TException {
		errorIfClosed();
		try {
			this.delegate.add(paramByteBuffer, paramColumnParent,
					paramCounterColumn, paramConsistencyLevel);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void remove(ByteBuffer paramByteBuffer, ColumnPath paramColumnPath,
			long paramLong, ConsistencyLevel paramConsistencyLevel)
			throws InvalidRequestException, UnavailableException,
			TimedOutException, TException {
		errorIfClosed();
		try {
			this.delegate.remove(paramByteBuffer, paramColumnPath, paramLong,
					paramConsistencyLevel);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void remove_counter(ByteBuffer paramByteBuffer,
			ColumnPath paramColumnPath, ConsistencyLevel paramConsistencyLevel)
			throws InvalidRequestException, UnavailableException,
			TimedOutException, TException {
		errorIfClosed();
		try {
			this.delegate.remove_counter(paramByteBuffer, paramColumnPath,
					paramConsistencyLevel);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void batch_mutate(
			Map<ByteBuffer, Map<String, List<org.apache.cassandra.thrift.Mutation>>> paramMap,
			ConsistencyLevel paramConsistencyLevel)
			throws InvalidRequestException, UnavailableException,
			TimedOutException, TException {
		errorIfClosed();
		try {
			this.delegate.batch_mutate(paramMap, paramConsistencyLevel);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void atomic_batch_mutate(
			Map<ByteBuffer, Map<String, List<org.apache.cassandra.thrift.Mutation>>> paramMap,
			ConsistencyLevel paramConsistencyLevel)
			throws InvalidRequestException, UnavailableException,
			TimedOutException, TException {
		errorIfClosed();
		try {
			this.delegate.atomic_batch_mutate(paramMap, paramConsistencyLevel);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public void truncate(String paramString) throws InvalidRequestException,
			UnavailableException, TimedOutException, TException {
		errorIfClosed();
		try {
			this.delegate.truncate(paramString);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	public Map<String, List<String>> describe_schema_versions()
			throws InvalidRequestException, TException {
		errorIfClosed();
		try {
			return this.delegate.describe_schema_versions();
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<KsDef> describe_keyspaces() throws InvalidRequestException,
			TException {
		errorIfClosed();
		try {
			return this.delegate.describe_keyspaces();
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public String describe_cluster_name() throws TException {
		errorIfClosed();
		try {
			return this.delegate.describe_cluster_name();
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public String describe_version() throws TException {
		errorIfClosed();
		try {
			return this.delegate.describe_version();
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<TokenRange> describe_ring(String paramString)
			throws InvalidRequestException, TException {
		errorIfClosed();
		try {
			return this.delegate.describe_ring(paramString);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public Map<String, String> describe_token_map()
			throws InvalidRequestException, TException {
		errorIfClosed();
		try {
			return this.delegate.describe_token_map();
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public String describe_partitioner() throws TException {
		errorIfClosed();
		try {
			return this.delegate.describe_partitioner();
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public String describe_snitch() throws TException {
		errorIfClosed();
		try {
			return this.delegate.describe_snitch();
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public KsDef describe_keyspace(String paramString)
			throws NotFoundException, InvalidRequestException, TException {
		errorIfClosed();
		try {
			return this.delegate.describe_keyspace(paramString);
		} catch (NotFoundException e) {
			handleException(e);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<String> describe_splits(String paramString1,
			String paramString2, String paramString3, int paramInt)
			throws InvalidRequestException, TException {
		errorIfClosed();
		try {
			return this.delegate.describe_splits(paramString1, paramString2,
					paramString3, paramInt);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public ByteBuffer trace_next_query() throws TException {
		errorIfClosed();
		try {
			return this.delegate.trace_next_query();
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public List<CfSplit> describe_splits_ex(String paramString1,
			String paramString2, String paramString3, int paramInt)
			throws InvalidRequestException, TException {
		errorIfClosed();
		try {
			return this.delegate.describe_splits_ex(paramString1, paramString2,
					paramString3, paramInt);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public String system_add_column_family(CfDef paramCfDef)
			throws InvalidRequestException, SchemaDisagreementException,
			TException {
		errorIfClosed();
		try {
			return this.delegate.system_add_column_family(paramCfDef);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (SchemaDisagreementException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public String system_drop_column_family(String paramString)
			throws InvalidRequestException, SchemaDisagreementException,
			TException {
		errorIfClosed();
		try {
			return this.delegate.system_drop_column_family(paramString);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (SchemaDisagreementException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public String system_add_keyspace(KsDef paramKsDef)
			throws InvalidRequestException, SchemaDisagreementException,
			TException {
		errorIfClosed();
		try {
			return this.delegate.system_add_keyspace(paramKsDef);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (SchemaDisagreementException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public String system_drop_keyspace(String paramString)
			throws InvalidRequestException, SchemaDisagreementException,
			TException {
		errorIfClosed();
		try {
			return this.delegate.system_drop_keyspace(paramString);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (SchemaDisagreementException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public String system_update_keyspace(KsDef paramKsDef)
			throws InvalidRequestException, SchemaDisagreementException,
			TException {
		errorIfClosed();
		try {
			return this.delegate.system_update_keyspace(paramKsDef);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (SchemaDisagreementException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public String system_update_column_family(CfDef paramCfDef)
			throws InvalidRequestException, SchemaDisagreementException,
			TException {
		errorIfClosed();
		try {
			return this.delegate.system_update_column_family(paramCfDef);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (SchemaDisagreementException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public CqlResult execute_cql_query(ByteBuffer paramByteBuffer,
			Compression paramCompression) throws InvalidRequestException,
			UnavailableException, TimedOutException,
			SchemaDisagreementException, TException {
		errorIfClosed();
		try {
			return this.delegate.execute_cql_query(paramByteBuffer,
					paramCompression);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (SchemaDisagreementException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public CqlResult execute_cql3_query(ByteBuffer paramByteBuffer,
			Compression paramCompression, ConsistencyLevel paramConsistencyLevel)
			throws InvalidRequestException, UnavailableException,
			TimedOutException, SchemaDisagreementException, TException {
		errorIfClosed();
		try {
			return this.delegate.execute_cql3_query(paramByteBuffer,
					paramCompression, paramConsistencyLevel);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (SchemaDisagreementException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public CqlPreparedResult prepare_cql_query(ByteBuffer paramByteBuffer,
			Compression paramCompression) throws InvalidRequestException,
			TException {
		errorIfClosed();
		try {
			return this.delegate.prepare_cql_query(paramByteBuffer,
					paramCompression);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public CqlPreparedResult prepare_cql3_query(ByteBuffer paramByteBuffer,
			Compression paramCompression) throws InvalidRequestException,
			TException {
		errorIfClosed();
		try {
			return this.delegate.prepare_cql3_query(paramByteBuffer,
					paramCompression);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public CqlResult execute_prepared_cql_query(int paramInt,
			List<ByteBuffer> paramList) throws InvalidRequestException,
			UnavailableException, TimedOutException,
			SchemaDisagreementException, TException {
		errorIfClosed();
		try {
			return this.delegate
					.execute_prepared_cql_query(paramInt, paramList);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (SchemaDisagreementException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public CqlResult execute_prepared_cql3_query(int paramInt,
			List<ByteBuffer> paramList, ConsistencyLevel paramConsistencyLevel)
			throws InvalidRequestException, UnavailableException,
			TimedOutException, SchemaDisagreementException, TException {
		errorIfClosed();
		try {
			return this.delegate.execute_prepared_cql3_query(paramInt,
					paramList, paramConsistencyLevel);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (UnavailableException e) {
			handleException(e);
		} catch (TimedOutException e) {
			handleException(e);
		} catch (SchemaDisagreementException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
		return null;
	}

	public void set_cql_version(String paramString)
			throws InvalidRequestException, TException {
		errorIfClosed();
		try {
			this.delegate.set_cql_version(paramString);
		} catch (InvalidRequestException e) {
			handleException(e);
		} catch (TException e) {
			handleException(e);
		}
	}

	protected void handleException(AuthenticationException e)
			throws AuthenticationException {
		throw e;
	}

	protected void handleException(AuthorizationException e)
			throws AuthorizationException {
		throw e;
	}

	protected void handleException(TException e) throws TException {
		throw e;
	}

	protected void handleException(InvalidRequestException e)
			throws InvalidRequestException {
		throw e;
	}

	protected void handleException(NotFoundException e)
			throws NotFoundException {
		throw e;
	}

	protected void handleException(UnavailableException e)
			throws UnavailableException {
		throw e;
	}

	protected void handleException(TimedOutException e)
			throws TimedOutException {
		throw e;
	}

	protected void handleException(SchemaDisagreementException e)
			throws SchemaDisagreementException {
		throw e;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("closed", closed);
		builder.append("ttransport", ttransport);
		return builder.toString();
	}
}
